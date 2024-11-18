package dev.cryptospace.tasket.server.routes

import PostgresIntegrationTest
import dev.cryptospace.module
import dev.cryptospace.tasket.payloads.TodoCommentPayload
import dev.cryptospace.tasket.server.table.TodosTable
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
    fun `get all comments on empty database should return empty array`() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        client.get("/todos/$todoId/comments").apply {
            assert(status == HttpStatusCode.OK)
            val payload = body<List<TodoCommentPayload>>()
            assert(payload.isEmpty())
        }
    }

    @Test
    fun `get all comments after post should return inserted item`() = testApplication {
        application {
            module()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        client.post("/todos/$todoId/comments") {
            contentType(ContentType.Application.Json)
            setBody(TodoCommentPayload(comment = "Test Comment"))
        }.apply {
            assert(status == HttpStatusCode.Created)
        }

        client.get("/todos/$todoId/comments").apply {
            assert(status == HttpStatusCode.OK)
            val payload = body<List<TodoCommentPayload>>()
            assert(payload.size == 1)
            assert(payload.first().comment == "Test Comment")
        }
    }
}
