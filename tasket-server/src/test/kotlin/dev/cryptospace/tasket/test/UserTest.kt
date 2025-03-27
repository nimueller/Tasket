package dev.cryptospace.tasket.test

import dev.cryptospace.tasket.payloads.authentication.LoginRequestPayload
import dev.cryptospace.tasket.payloads.authentication.LoginResponsePayload
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.ApplicationTestBuilder

suspend fun ApplicationTestBuilder.authenticateTestUser(user: TestUser) {
    val client = createClient {
        install(ContentNegotiation) {
            json()
        }
    }

    val response = client.post("/rest/login") {
        contentType(ContentType.Application.Json)
        setBody(LoginRequestPayload(username = user.username, password = user.password))
    }
    val responseBody = response.body<LoginResponsePayload>()

}
