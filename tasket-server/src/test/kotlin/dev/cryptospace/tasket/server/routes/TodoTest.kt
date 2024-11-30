package dev.cryptospace.tasket.server.routes

import dev.cryptospace.tasket.payloads.TodoPayload
import dev.cryptospace.tasket.test.PostgresIntegrationTest
import dev.cryptospace.tasket.test.testWebserviceAuthenticated
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test

@ExtendWith(PostgresIntegrationTest::class)
class TodoTest {

    @Test
    fun `get all todos on empty database should return empty array`() = testWebserviceAuthenticated {
        get("/rest/todos").apply {
            assert(status == HttpStatusCode.OK)
            val payload = body<List<TodoPayload>>()
            assert(payload.isEmpty())
        }
    }

    @Test
    fun `get all todos after post should return inserted item`() = testWebserviceAuthenticated {
        post("/rest/todos") {
            contentType(ContentType.Application.Json)
            setBody(TodoPayload(label = "Test Todo", statusId = null))
        }.apply {
            assert(status == HttpStatusCode.Created)
        }

        get("/rest/todos").apply {
            assert(status == HttpStatusCode.OK)
            val payload = body<List<TodoPayload>>()
            assert(payload.size == 1)
            assert(payload.first().label == "Test Todo")
        }
    }

    @Test
    fun `get inserted todo after post should return inserted item`() = testWebserviceAuthenticated {
        val insertedItem = post("/rest/todos") {
            contentType(ContentType.Application.Json)
            setBody(TodoPayload(label = "Test Todo", statusId = null))
        }.let { response ->
            assert(response.status == HttpStatusCode.Created)
            response.body<TodoPayload>()
        }

        get("/rest/todos/${insertedItem.id}").apply {
            assert(status == HttpStatusCode.OK)
            val payload = body<TodoPayload>()
            assert(payload.label == "Test Todo")
        }
    }

    @Test
    fun `get todo with invalid UUID should return 400`() = testWebserviceAuthenticated {
        get("/rest/todos/null").apply {
            assert(status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun `get todo which does not exist should return 404`() = testWebserviceAuthenticated {
        get("/rest/todos/00000000-0000-0000-0000-000000000000").apply {
            assert(status == HttpStatusCode.NotFound)
        }
    }

    @Test
    fun `delete todo on existing item should remove item`() = testWebserviceAuthenticated {
        val insertedItem = post("/rest/todos") {
            contentType(ContentType.Application.Json)
            setBody(TodoPayload(label = "Test Todo", statusId = null))
        }.let { response ->
            assert(response.status == HttpStatusCode.Created)
            response.body<TodoPayload>()
        }

        delete("/rest/todos/${insertedItem.id}").apply {
            assert(status == HttpStatusCode.OK)
        }

        get("/rest/todos/${insertedItem.id}").apply {
            assert(status == HttpStatusCode.NotFound)
        }
    }

    @Test
    fun `delete todo which does not exist should return 404`() = testWebserviceAuthenticated {
        delete("/rest/todos/00000000-0000-0000-0000-000000000000").apply {
            assert(status == HttpStatusCode.NotFound)
        }
    }
}
