package dev.cryptospace.tasket.test

import dev.cryptospace.module
import dev.cryptospace.tasket.payloads.LoginRequestPayload
import dev.cryptospace.tasket.server.table.UsersTable
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
import io.ktor.server.testing.testApplication
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import java.util.UUID

const val TEST_USER_USERNAME = "test"
const val TEST_USER_PASSWORD = "password"
private const val TEST_USER_PASSWORD_HASH = "l2sr83tTVOdVmiMZwhcQyjn5NFJhSsP0M3ZvlEYBQ1g="
private const val TEST_USER_PASSWORD_SALT = "q/zMS1D1fqwuO1qcb1Rp4A=="

class AuthenticatedIntegrationTest :
    BeforeAllCallback,
    AfterAllCallback {

    private lateinit var testUserId: UUID

    override fun beforeAll(context: ExtensionContext) {
        testUserId = transaction {
            UsersTable.insert {
                it[username] = TEST_USER_USERNAME
                it[password] = TEST_USER_PASSWORD_HASH
                it[salt] = TEST_USER_PASSWORD_SALT
            }[UsersTable.id].value
        }
    }

    override fun afterAll(context: ExtensionContext) {
        transaction {
            UsersTable.deleteWhere { UsersTable.id eq testUserId }
        }
    }
}

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
    application {
        module()
    }

    val loginClient = createClient {
        install(ContentNegotiation) {
            json()
        }
    }

    val token = loginClient.post("/login") {
        contentType(ContentType.Application.Json)
        setBody(LoginRequestPayload(username = TEST_USER_USERNAME, password = TEST_USER_PASSWORD))
    }.body<String>()

    val client = createClient {
        install(ContentNegotiation) {
            json()
        }
        defaultRequest {
            bearerAuth(token)
        }
    }

    client.doTest()
}
