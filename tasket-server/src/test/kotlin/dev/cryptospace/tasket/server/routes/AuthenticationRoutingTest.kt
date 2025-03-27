package dev.cryptospace.tasket.server.routes

import com.auth0.jwt.JWT
import dev.cryptospace.tasket.payloads.authentication.LoginRequestPayload
import dev.cryptospace.tasket.payloads.authentication.LoginResponsePayload
import dev.cryptospace.tasket.test.PostgresIntegrationTest
import dev.cryptospace.tasket.test.UserTests
import dev.cryptospace.tasket.test.testWebservice
import dev.cryptospace.tasket.test.testWebserviceAuthenticated
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test

@ExtendWith(PostgresIntegrationTest::class)
class AuthenticationRoutingTest : UserTests() {

    @Test
    fun `unknown user should return 401`() = testWebservice {
        val response = client.post("/rest/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequestPayload(username = "unknown", password = "unknown"))
        }

        assert(response.status == HttpStatusCode.Unauthorized)
    }

    @Test
    fun `invalid password should return 401`() = testWebservice {
        val adminUser = insertUser(username = "admin", password = "admin")

        val response = client.post("/rest/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequestPayload(username = adminUser.username, password = "invalid"))
        }

        assert(response.status == HttpStatusCode.Unauthorized)
    }

    @Test
    fun `valid credentials should return valid access and refresh tokens`() = testWebservice {
        val adminUser = insertUser(username = "admin", password = "admin")

        val response = client.post("/rest/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequestPayload(username = "admin", password = "admin"))
        }

        assert(response.status == HttpStatusCode.OK)
        val tokens = response.body<LoginResponsePayload>()
        assert(JWT.decode(tokens.accessToken).subject == adminUser.id.value.toString())
        assert(JWT.decode(tokens.refreshToken).subject == adminUser.id.value.toString())
    }


    @Test
    fun `refresh should return valid access token and rotate refresh token`() =
        testWebserviceAuthenticated { user ->
            post("/rest/refresh") {
                contentType(ContentType.Application.Json)
//                setBody(RefreshTokenRequestPayload(user.refreshToken))
            }.apply {
                assert(status == HttpStatusCode.OK)
                val responsePayload = body<LoginResponsePayload>()
                val decodedAccessToken = JWT.decode(responsePayload.accessToken)
                assert(decodedAccessToken.subject == user.id.value.toString())

                val decodedRefreshToken = JWT.decode(responsePayload.refreshToken)
                assert(decodedRefreshToken.subject == user.id.value.toString())
            }
        }
}
