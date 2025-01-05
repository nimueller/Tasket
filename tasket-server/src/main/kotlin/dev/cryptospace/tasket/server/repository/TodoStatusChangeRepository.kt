package dev.cryptospace.tasket.server.repository

import dev.cryptospace.tasket.payloads.todo.response.TodoStatusChangeResponsePayload
import dev.cryptospace.tasket.server.todo.database.TodoStatusChangesTable
import dev.cryptospace.tasket.server.todo.mapper.TodoStatusChangeResponseMapper
import java.util.UUID

object TodoStatusChangeRepository : ReadOnlyRepository<TodoStatusChangesTable, TodoStatusChangeResponsePayload>(
    TodoStatusChangesTable,
    TodoStatusChangeResponseMapper,
) {
    suspend fun getAllCommentsForTodo(todoId: UUID): List<TodoStatusChangeResponsePayload> {
        return queryForMultipleResults {
            TodoStatusChangesTable.todoId eq todoId
        }
    }
}
