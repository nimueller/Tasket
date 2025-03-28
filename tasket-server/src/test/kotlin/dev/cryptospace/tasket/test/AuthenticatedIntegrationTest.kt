package dev.cryptospace.tasket.test

import dev.cryptospace.module
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.bearerAuth
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication

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

fun testAuthenticatedWebservice(
    asUsername: String = TEST_USER_USERNAME,
    asAdmin: Boolean = false,
    doTest: suspend WebserviceTest.(TestUser) -> Unit,
) {
    val user = insertUser(username = asUsername, isAdmin = asAdmin)
    testAuthenticatedWebservice(user = user, doTest = doTest)
}

fun testAuthenticatedWebservice(user: TestUser, doTest: suspend WebserviceTest.(TestUser) -> Unit) = testApplication {
    application {
        module()
    }

    val unauthenticatedClient = createClient {
        install(ContentNegotiation) {
            json()
        }
    }

    val tokens = with(WebserviceTest(unauthenticatedClient)) { user.login() }

    val authenticatedClient = createClient {
        install(ContentNegotiation) {
            json()
        }
        defaultRequest {
            bearerAuth(tokens.accessToken)
        }
    }

    val test = WebserviceTest(authenticatedClient)
    test.doTest(user)
}
