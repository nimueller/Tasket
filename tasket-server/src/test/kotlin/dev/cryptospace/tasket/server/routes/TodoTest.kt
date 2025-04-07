package dev.cryptospace.tasket.server.routes

import dev.cryptospace.tasket.payloads.OptionalField
import dev.cryptospace.tasket.payloads.todo.request.TodoPatchRequestPayload
import dev.cryptospace.tasket.payloads.todo.request.TodoRequestPayload
import dev.cryptospace.tasket.payloads.todo.response.TodoResponsePayload
import dev.cryptospace.tasket.test.PostgresIntegrationTest
import dev.cryptospace.tasket.test.insertTodo
import dev.cryptospace.tasket.test.insertUser
import dev.cryptospace.tasket.test.testAuthenticatedWebservice
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test

@ExtendWith(PostgresIntegrationTest::class)
class TodoTest {

    @Test
    fun `get all todos on empty database should return empty array`() = testAuthenticatedWebservice {
        val response = client.get("/rest/todos")

        assert(response.status == HttpStatusCode.OK)
        val payload = response.body<List<TodoRequestPayload>>()
        assert(payload.isEmpty())
    }

    @Test
    fun `user should only see own todos when querying all todos`() = testAuthenticatedWebservice { ownUser ->
        val ownTodoId = insertTodo(owner = ownUser)
        val otherUser = insertUser(username = "other")
        val otherTodoId = insertTodo(owner = otherUser)

        val response = client.get("/rest/todos")

        assert(response.status == HttpStatusCode.OK)
        val payload = response.body<List<TodoResponsePayload>>()
        assert(payload.size == 1)
        assert(payload.first().metaInformation.id == ownTodoId)
        assert(payload.none { it.metaInformation.id == otherTodoId })
    }

    @Test
    fun `user should not be able to see todo of other users`() = testAuthenticatedWebservice {
        val otherUser = insertUser(username = "other")
        val otherTodoId = insertTodo(owner = otherUser)

        val response = client.get("/rest/todos/$otherTodoId")

        assert(response.status == HttpStatusCode.NotFound)
    }

    @Test
    fun `user should not be able to update todo of other users`() = testAuthenticatedWebservice {
        val otherUser = insertUser(username = "other")
        val otherTodoId = insertTodo(owner = otherUser)

        val response = client.put("/rest/todos/$otherTodoId") {
            contentType(ContentType.Application.Json)
            setBody(TodoRequestPayload(label = "Test Todo", statusId = null))
        }

        assert(response.status == HttpStatusCode.NotFound)
    }

    @Test
    fun `user should not be able to delete todo of other users`() = testAuthenticatedWebservice {
        val otherUser = insertUser(username = "other")
        val otherTodoId = insertTodo(owner = otherUser)

        val response = client.delete("/rest/todos/$otherTodoId")

        assert(response.status == HttpStatusCode.NotFound)
    }

    @Test
    fun `get all todos after post should return inserted item`() = testAuthenticatedWebservice {
        client.post("/rest/todos") {
            contentType(ContentType.Application.Json)
            setBody(TodoRequestPayload(label = "Test Todo", statusId = null))
        }.apply {
            assert(status == HttpStatusCode.Created)
        }

        client.get("/rest/todos").apply {
            assert(status == HttpStatusCode.OK)
            val payload = body<List<TodoResponsePayload>>()
            assert(payload.size == 1)
            assert(payload.first().label == "Test Todo")
        }
    }

    @Test
    fun `get inserted todo after post should return inserted item`() = testAuthenticatedWebservice {
        val insertedItem = client.post("/rest/todos") {
            contentType(ContentType.Application.Json)
            setBody(TodoRequestPayload(label = "Test Todo", statusId = null))
        }.let { response ->
            assert(response.status == HttpStatusCode.Created)
            response.body<TodoResponsePayload>()
        }

        client.get("/rest/todos/${insertedItem.metaInformation.id}").apply {
            assert(status == HttpStatusCode.OK)
            val payload = body<TodoResponsePayload>()
            assert(payload.label == "Test Todo")
        }
    }

    @Test
    fun `get todo with invalid UUID should return 400`() = testAuthenticatedWebservice {
        client.get("/rest/todos/null").apply {
            assert(status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun `get todo which does not exist should return 404`() = testAuthenticatedWebservice {
        client.get("/rest/todos/00000000-0000-0000-0000-000000000000").apply {
            assert(status == HttpStatusCode.NotFound)
        }
    }

    @Test
    fun `patch todo label on existing item should update only label`() = testAuthenticatedWebservice { ownUser ->
        val ownTodoId = insertTodo(owner = ownUser, label = "Test Todo")

        val response = client.patch("/rest/todos/$ownTodoId") {
            contentType(ContentType.Application.Json)
            setBody(TodoPatchRequestPayload(label = OptionalField.of("Other Todo")))
        }

        assert(response.status == HttpStatusCode.OK)
        val payload = response.body<TodoResponsePayload>()
        assert(payload.label == "Other Todo")
        assert(payload.statusId == "1fab4c54-11bc-4f8a-a950-7a0032d31e85")
    }

    @Test
    fun `patch todo status on existing item should update only status`() = testAuthenticatedWebservice { ownUser ->
        val ownTodoId = insertTodo(owner = ownUser, label = "Test Todo")

        val response = client.patch("/rest/todos/$ownTodoId") {
            contentType(ContentType.Application.Json)
            setBody(TodoPatchRequestPayload(statusId = OptionalField.of("a7226e20-0ba5-4f20-b69e-84243d207d7f")))
        }

        assert(response.status == HttpStatusCode.OK)
        val payload = response.body<TodoResponsePayload>()
        assert(payload.label == "Test Todo")
        assert(payload.statusId == "a7226e20-0ba5-4f20-b69e-84243d207d7f")
    }

    @Test
    fun `delete todo on existing item should remove item`() = testAuthenticatedWebservice {
        val insertedItem = client.post("/rest/todos") {
            contentType(ContentType.Application.Json)
            setBody(TodoRequestPayload(label = "Test Todo", statusId = null))
        }.let { response ->
            assert(response.status == HttpStatusCode.Created)
            response.body<TodoResponsePayload>()
        }

        client.delete("/rest/todos/${insertedItem.metaInformation.id}").apply {
            assert(status == HttpStatusCode.OK)
        }

        client.get("/rest/todos/${insertedItem.metaInformation.id}").apply {
            assert(status == HttpStatusCode.NotFound)
        }
    }

    @Test
    fun `delete todo which does not exist should return 404`() = testAuthenticatedWebservice {
        client.delete("/rest/todos/00000000-0000-0000-0000-000000000000").apply {
            assert(status == HttpStatusCode.NotFound)
        }
    }

}
