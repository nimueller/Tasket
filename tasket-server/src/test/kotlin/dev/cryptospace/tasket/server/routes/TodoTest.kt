package dev.cryptospace.tasket.server.routes

import PostgresIntegrationTest
import dev.cryptospace.module
import dev.cryptospace.tasket.payloads.TodoPayload
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test

@ExtendWith(PostgresIntegrationTest::class)
class TodoTest {

    @Test
    fun `get all todos on empty database should return empty array`() = testApplication {
        application {
            module()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        client.get("/todos").apply {
            assert(status == HttpStatusCode.OK)
            val payload = body<List<TodoPayload>>()
            assert(payload.isEmpty())
        }
    }

    @Test
    fun `get all todos after post should return inserted item`() = testApplication {
        application {
            module()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        client.post("/todos") {
            contentType(ContentType.Application.Json)
            setBody(TodoPayload(label = "Test Todo"))
        }.apply {
            assert(status == HttpStatusCode.Created)
        }

        client.get("/todos").apply {
            assert(status == HttpStatusCode.OK)
            val payload = body<List<TodoPayload>>()
            assert(payload.size == 1)
            assert(payload.first().label == "Test Todo")
        }
    }

    @Test
    fun `get inserted todo after post should return inserted item`() = testApplication {
        application {
            module()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val insertedItem = client.post("/todos") {
            contentType(ContentType.Application.Json)
            setBody(TodoPayload(label = "Test Todo"))
        }.let { response ->
            assert(response.status == HttpStatusCode.Created)
            response.body<TodoPayload>()
        }

        client.get("/todos/${insertedItem.id}").apply {
            assert(status == HttpStatusCode.OK)
            val payload = body<TodoPayload>()
            assert(payload.label == "Test Todo")
        }
    }

    @Test
    fun `get todo with invalid UUID should return 400`() = testApplication {
        application {
            module()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        client.get("/todos/null").apply {
            assert(status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun `get todo which does not exist should return 404`() = testApplication {
        application {
            module()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        client.get("/todos/00000000-0000-0000-0000-000000000000").apply {
            assert(status == HttpStatusCode.NotFound)
        }
    }
}
