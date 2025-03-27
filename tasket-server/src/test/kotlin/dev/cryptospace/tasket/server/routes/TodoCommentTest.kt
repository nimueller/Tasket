package dev.cryptospace.tasket.server.routes

import dev.cryptospace.tasket.payloads.todo.request.TodoCommentRequestPayload
import dev.cryptospace.tasket.payloads.todo.response.TodoCommentResponsePayload
import dev.cryptospace.tasket.server.todo.database.TodosTable
import dev.cryptospace.tasket.test.PostgresIntegrationTest
import dev.cryptospace.tasket.test.TestUser
import dev.cryptospace.tasket.test.testWebserviceAuthenticated
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.extension.ExtendWith
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test

@ExtendWith(PostgresIntegrationTest::class)
class TodoCommentTest {

    private lateinit var todoId: UUID
    private lateinit var user: TestUser

    @BeforeTest
    fun setup() {
        transaction {
            todoId = TodosTable.insert {
                it[label] = "Test Todo"
                it[owner] = user.id.value
            }[TodosTable.id].value
        }
        println("Todo ID: $todoId")
    }

    @Test
    fun `get all comments on empty database should return empty array`() = testWebserviceAuthenticated(user) {
        get("/rest/todos/$todoId/comments").apply {
            assert(status == HttpStatusCode.OK)
            val payload = body<List<TodoCommentResponsePayload>>()
            assert(payload.isEmpty())
        }
    }

    @Test
    fun `get all comments after post should return inserted item`() = testWebserviceAuthenticated(user) {
        post("/rest/todos/$todoId/comments") {
            contentType(ContentType.Application.Json)
            setBody(TodoCommentRequestPayload(comment = "Test Comment"))
        }.apply {
            assert(status == HttpStatusCode.Created)
        }

        get("/rest/todos/$todoId/comments").apply {
            assert(status == HttpStatusCode.OK)
            val payload = body<List<TodoCommentResponsePayload>>()
            assert(payload.size == 1)
            assert(payload.first().comment == "Test Comment")
        }
    }
}
