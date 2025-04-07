package dev.cryptospace.tasket.server.routes

import dev.cryptospace.tasket.payloads.todo.request.TodoCommentRequestPayload
import dev.cryptospace.tasket.payloads.todo.request.TodoRequestPayload
import dev.cryptospace.tasket.payloads.todo.response.TodoCommentResponsePayload
import dev.cryptospace.tasket.test.PostgresIntegrationTest
import dev.cryptospace.tasket.test.insertTodo
import dev.cryptospace.tasket.test.insertUser
import dev.cryptospace.tasket.test.testAuthenticatedWebservice
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test

@ExtendWith(PostgresIntegrationTest::class)
class TodoCommentTest {

    @Test
    fun `get all comments on empty database should return empty array`() = testAuthenticatedWebservice { user ->
        val todoId = insertTodo(user)

        val response = client.get("/rest/todos/$todoId/comments")

        assert(response.status == HttpStatusCode.OK)
        val payload = response.body<List<TodoCommentResponsePayload>>()
        assert(payload.isEmpty())
    }

    @Test
    fun `user should not be able to see todo comments of other users`() = testAuthenticatedWebservice { user ->
        val otherUser = insertUser(username = "other")
        val otherTodoId = insertTodo(owner = otherUser)

        val response = client.get("/rest/todos/$otherTodoId/comments")

        assert(response.status == HttpStatusCode.NotFound)
    }

    @Test
    fun `user should not be able to update todo comments of other users`() = testAuthenticatedWebservice { user ->
        val otherUser = insertUser(username = "other")
        val otherTodoId = insertTodo(owner = otherUser)

        val response = client.put("/rest/todos/$otherTodoId/comments") {
            contentType(ContentType.Application.Json)
            setBody(TodoRequestPayload(label = "Test Todo"))
        }

        assert(response.status == HttpStatusCode.NotFound)
    }

    @Test
    fun `user should not be able to delete todo comments of other users`() = testAuthenticatedWebservice { user ->
        val otherUser = insertUser(username = "other")
        val otherTodoId = insertTodo(owner = otherUser)

        val response = client.delete("/rest/todos/$otherTodoId/comments")

        assert(response.status == HttpStatusCode.NotFound)
    }

    @Test
    fun `get all comments after post should return inserted item`() = testAuthenticatedWebservice { user ->
        val todoId = insertTodo(user)

        client.post("/rest/todos/$todoId/comments") {
            contentType(ContentType.Application.Json)
            setBody(TodoCommentRequestPayload(comment = "Test Comment"))
        }.apply {
            assert(status == HttpStatusCode.Created)
        }

        client.get("/rest/todos/$todoId/comments").apply {
            assert(status == HttpStatusCode.OK)
            val payload = body<List<TodoCommentResponsePayload>>()
            assert(payload.size == 1)
            assert(payload.first().comment == "Test Comment")
        }
    }
}
