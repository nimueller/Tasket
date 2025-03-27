package dev.cryptospace.tasket.test

import dev.cryptospace.module
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.bearerAuth
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import org.jetbrains.exposed.sql.transactions.transaction

fun testWebservice(doTest: suspend WebserviceTest.() -> Unit) = testApplication {
    application {
        module()
    }

    val client = createClient {
        install(ContentNegotiation) {
            json()
        }
    }

    val test = WebserviceTest(client)
    test.doTest()
}

fun testWebserviceAuthenticated(
    username: String = TEST_USER_USERNAME,
    isAdmin: Boolean = false,
    doTest: suspend HttpClient.(TestUser) -> Unit
) = testApplication {
}

fun testWebserviceAuthenticated(
    user: TestUser,
    doTest: suspend HttpClient.(TestUser) -> Unit,
) = testApplication {
    authenticateTestUser(user)
    cleanupAuthenticatedWebservice()

    application {
        module()
    }

//    val client = prepareAuthenticatedClient(user.accessToken)
    client.doTest(user)
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
