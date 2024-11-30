package dev.cryptospace.tasket.server.routes

import dev.cryptospace.tasket.payloads.TodoPayload
import dev.cryptospace.tasket.payloads.TodoStatusChangePayload
import dev.cryptospace.tasket.server.table.TodosTable
import dev.cryptospace.tasket.test.PostgresIntegrationTest
import dev.cryptospace.tasket.test.testWebserviceAuthenticated
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.put
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

private const val NEW_STATUS_ID = "1fab4c54-11bc-4f8a-a950-7a0032d31e85"
private const val IN_PROGRESS_STATUS_ID = "a7226e20-0ba5-4f20-b69e-84243d207d7f"

@ExtendWith(PostgresIntegrationTest::class)
class TodoStatusChangeTest {

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
    fun `initial status should be New`() = testWebserviceAuthenticated {
        get("/rest/todos/$todoId").apply {
            assert(status == HttpStatusCode.OK)
            val payload = body<TodoPayload>()
            assert(payload.statusId == NEW_STATUS_ID)
        }
    }

    @Test
    fun `get status changes on new todo should return empty list`() = testWebserviceAuthenticated {
        get("/rest/todos/$todoId/statusChanges").apply {
            assert(status == HttpStatusCode.OK)
            val payload = body<List<TodoStatusChangePayload>>()
            assert(payload.isEmpty())
        }
    }

    @Test
    fun `get status changes on todo with changed status should return change`() = testWebserviceAuthenticated {
        put("/rest/todos/$todoId") {
            contentType(ContentType.Application.Json)
            setBody(TodoPayload(label = "Test Todo", statusId = IN_PROGRESS_STATUS_ID))
        }.apply {
            assert(status == HttpStatusCode.OK)
            assert(body<TodoPayload>().statusId == IN_PROGRESS_STATUS_ID)
        }

        get("/rest/todos/$todoId/statusChanges").apply {
            assert(status == HttpStatusCode.OK)
            val payload = body<List<TodoStatusChangePayload>>()
            assert(payload.size == 1)
            assert(payload.first().oldStatus == NEW_STATUS_ID)
            assert(payload.first().newStatus == IN_PROGRESS_STATUS_ID)
        }
    }
}
