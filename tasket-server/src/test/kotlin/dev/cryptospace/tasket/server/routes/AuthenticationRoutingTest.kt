package dev.cryptospace.tasket.server.routes

import com.auth0.jwt.JWT
import dev.cryptospace.tasket.payloads.authentication.LoginRequestPayload
import dev.cryptospace.tasket.payloads.authentication.LoginResponsePayload
import dev.cryptospace.tasket.payloads.authentication.RefreshTokenRequestPayload
import dev.cryptospace.tasket.test.PostgresIntegrationTest
import dev.cryptospace.tasket.test.testWebserviceAuthenticatedWithUser
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test

@ExtendWith(PostgresIntegrationTest::class)
class AuthenticationRoutingTest {

    @Test
    fun `valid credentials should return valid access token`() = testWebserviceAuthenticatedWithUser { user ->

        post("/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequestPayload(username = user.username, password = user.password))
        }.apply {
            assert(status == HttpStatusCode.OK)
            val responsePayload = body<LoginResponsePayload>()
            assert(JWT.decode(responsePayload.accessToken).subject == user.id.value.toString())
            assert(JWT.decode(responsePayload.refreshToken).subject == user.id.value.toString())
        }
    }

    @Test
    fun `unknown user should return 401`() = testWebserviceAuthenticatedWithUser { user ->
        post("/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequestPayload(username = "invalid", password = user.password))
        }.apply {
            assert(status == HttpStatusCode.Unauthorized)
        }
    }

    @Test
    fun `invalid password should return 401`() = testWebserviceAuthenticatedWithUser { user ->
        post("/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequestPayload(username = user.username, password = "invalid"))
        }.apply {
            assert(status == HttpStatusCode.Unauthorized)
        }
    }

    @Test
    fun `refresh should return valid access token and rotate refresh token`() =
        testWebserviceAuthenticatedWithUser { user ->
            post("/refresh") {
                contentType(ContentType.Application.Json)
                setBody(RefreshTokenRequestPayload(user.refreshToken))
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
