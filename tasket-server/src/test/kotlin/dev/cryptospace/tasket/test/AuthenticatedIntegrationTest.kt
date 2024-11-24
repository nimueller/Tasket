package dev.cryptospace.tasket.test

import dev.cryptospace.module
import dev.cryptospace.tasket.payloads.authentication.LoginRequestPayload
import dev.cryptospace.tasket.payloads.authentication.LoginResponsePayload
import dev.cryptospace.tasket.server.table.user.UserId
import dev.cryptospace.tasket.server.table.user.UserRole
import dev.cryptospace.tasket.server.table.user.UsersTable
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

const val TEST_USER_USERNAME = "test"
const val TEST_USER_PASSWORD = "password"
private const val TEST_USER_PASSWORD_HASH = "l2sr83tTVOdVmiMZwhcQyjn5NFJhSsP0M3ZvlEYBQ1g="
private const val TEST_USER_PASSWORD_SALT = "q/zMS1D1fqwuO1qcb1Rp4A=="

fun testWebserviceUnauthenticated(doTest: suspend HttpClient.() -> Unit) = testApplication {
    application {
        module()
    }

    val client = createClient {
        install(ContentNegotiation) {
            json()
        }
    }

    client.doTest()
}

fun testWebserviceAuthenticated(doTest: suspend HttpClient.() -> Unit) = testApplication {
    testWebserviceAuthenticatedWithUser { doTest() }
}

fun testWebserviceAuthenticatedWithUser(
    username: String = TEST_USER_USERNAME,
    isAdmin: Boolean = false,
    doTest: suspend HttpClient.(TestUser) -> Unit,
) = testApplication {
    application {
        module()
    }

    val testUserId = prepareTestUser(username, isAdmin)
    val loginResponse = authenticate(username)
    val client = prepareAuthenticatedClient(loginResponse.accessToken)

    try {
        client.doTest(
            TestUser(
                id = testUserId,
                username = username,
                password = TEST_USER_PASSWORD,
                accessToken = loginResponse.accessToken,
                refreshToken = loginResponse.refreshToken,
            ),
        )
    } finally {
        cleanupAuthenticatedWebservice()
    }
}

private fun prepareTestUser(user: String, isAdmin: Boolean): UserId {
    val testUserId = transaction {
        val insertedId = UsersTable.insert {
            it[username] = user
            it[password] = TEST_USER_PASSWORD_HASH
            it[salt] = TEST_USER_PASSWORD_SALT
            it[role] = if (isAdmin) UserRole.ADMIN else UserRole.USER
        }[UsersTable.id]

        UserId(insertedId.value)
    }
    return testUserId
}

private suspend fun ApplicationTestBuilder.authenticate(user: String): LoginResponsePayload {
    val client = createClient {
        install(ContentNegotiation) {
            json()
        }
    }

    return client.post("/login") {
        contentType(ContentType.Application.Json)
        setBody(LoginRequestPayload(username = user, password = TEST_USER_PASSWORD))
    }.body<LoginResponsePayload>()
}

private fun ApplicationTestBuilder.prepareAuthenticatedClient(accessToken: String): HttpClient {
    return createClient {
        install(ContentNegotiation) {
            json()
        }
        defaultRequest {
            bearerAuth(accessToken)
        }
    }
}

private fun cleanupAuthenticatedWebservice() {
    transaction {
        exec("TRUNCATE TABLE tasket.users CASCADE")
        println("Cleaned up all users")
    }
}

data class TestUser(
    val id: UserId,
    val username: String,
    val password: String,
    val accessToken: String,
    val refreshToken: String,
)
