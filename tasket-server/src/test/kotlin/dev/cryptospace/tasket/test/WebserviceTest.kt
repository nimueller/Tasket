package dev.cryptospace.tasket.test

import dev.cryptospace.tasket.payloads.authentication.LoginResponsePayload
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import org.intellij.lang.annotations.Language
import org.jetbrains.annotations.Contract
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.fail

const val TEST_USER_USERNAME = "test"
const val TEST_USER_PASSWORD = "password"

data class WebserviceTest(val client: HttpClient) {

    /**
     * Authenticate this [TestUser] return the access and refresh tokens.
     */
    @Contract(pure = true)
    suspend fun TestUser.login(): Tokens {
        return login(username, password)
    }

    /**
     * Authenticate a user with the given [username] and [password] and return the access and refresh tokens.
     * Fails the test if the authentication fails.
     */
    @Contract(pure = true)
    suspend fun login(username: String, password: String): Tokens {
        val tokens = tryLogin(username, password)

        if (tokens == null) {
            fail("Login failed")
        }

        return tokens
    }

    /**
     * Try to authenticate a user with the given [username] and [password] and return the access and refresh tokens if
     * successful. Returns `null` if the authentication failed.
     */
    @Contract(pure = true)
    suspend fun tryLogin(username: String, password: String): Tokens? {
        transaction {
            exec("SELECT * FROM tasket.users", transform = { resultSet ->
                while (resultSet.next()) {
                    println(resultSet.getString("username"))
                    println(resultSet.getString("password"))
                }
            })
        }
        val response = client.post("/rest/login") {
            @Language("json")
            val body = """
                {
                    "username": "$username",
                    "password": "$password"
                }
            """.trimIndent()

            contentType(ContentType.Application.Json)
            setBody(body)
        }

        if (!response.status.isSuccess()) {
            return null
        }

        assert(response.contentType()?.match(ContentType.Application.Json) == true)
        val responseBody = response.body<LoginResponsePayload>()

        return Tokens(responseBody.accessToken, responseBody.refreshToken)
    }
}
