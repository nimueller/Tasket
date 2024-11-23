package dev.cryptospace.tasket.server.routes

import com.auth0.jwt.JWT
import dev.cryptospace.tasket.payloads.LoginRequestPayload
import dev.cryptospace.tasket.test.AuthenticatedIntegrationTest
import dev.cryptospace.tasket.test.PostgresIntegrationTest
import dev.cryptospace.tasket.test.TEST_USER_PASSWORD
import dev.cryptospace.tasket.test.TEST_USER_USERNAME
import dev.cryptospace.tasket.test.testWebserviceUnauthenticated
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test

@ExtendWith(PostgresIntegrationTest::class)
@ExtendWith(AuthenticatedIntegrationTest::class)
class AuthenticationRoutingTest {

    @Test
    fun `invalid credentials should return JWT`() = testWebserviceUnauthenticated {
        post("/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequestPayload(username = TEST_USER_USERNAME, password = TEST_USER_PASSWORD))
        }.apply {
            assert(status == HttpStatusCode.OK)
            assert(JWT.decode(body<String>()).subject.isNotBlank())
        }
    }

    @Test
    fun `unknown user should return 401`() = testWebserviceUnauthenticated {
        post("/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequestPayload(username = "invalid", password = TEST_USER_PASSWORD))
        }.apply {
            assert(status == HttpStatusCode.Unauthorized)
        }
    }

    @Test
    fun `invalid password should return 401`() = testWebserviceUnauthenticated {
        post("/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequestPayload(username = TEST_USER_USERNAME, password = "invalid"))
        }.apply {
            assert(status == HttpStatusCode.Unauthorized)
        }
    }
}
