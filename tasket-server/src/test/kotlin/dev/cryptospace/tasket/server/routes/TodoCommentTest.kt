package dev.cryptospace.tasket.server.routes

import dev.cryptospace.tasket.payloads.TodoCommentPayload
import dev.cryptospace.tasket.server.table.TodosTable
import dev.cryptospace.tasket.test.PostgresIntegrationTest
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

    @BeforeTest
    fun setup() {
        transaction {
            todoId = TodosTable.insert {
                it[label] = "Test Todo"
            }[TodosTable.id].value
        }
        println("Todo ID: $todoId")
    }

    @Test
    fun `get all comments on empty database should return empty array`() = testWebserviceAuthenticated {
        get("/rest/todos/$todoId/comments").apply {
            assert(status == HttpStatusCode.OK)
            val payload = body<List<TodoCommentPayload>>()
            assert(payload.isEmpty())
        }
    }

    @Test
    fun `get all comments after post should return inserted item`() = testWebserviceAuthenticated {
        post("/rest/todos/$todoId/comments") {
            contentType(ContentType.Application.Json)
            setBody(TodoCommentPayload(comment = "Test Comment"))
        }.apply {
            assert(status == HttpStatusCode.Created)
        }

        get("/rest/todos/$todoId/comments").apply {
            assert(status == HttpStatusCode.OK)
            val payload = body<List<TodoCommentPayload>>()
            assert(payload.size == 1)
            assert(payload.first().comment == "Test Comment")
        }
    }
}
