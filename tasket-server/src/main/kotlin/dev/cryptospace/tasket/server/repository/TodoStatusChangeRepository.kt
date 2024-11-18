package dev.cryptospace.tasket.server.repository

import dev.cryptospace.tasket.payloads.TodoStatusChangePayload
import dev.cryptospace.tasket.server.payload.TodoStatusChangeMapper
import dev.cryptospace.tasket.server.table.TodoStatusChangesTable
import java.util.UUID

object TodoStatusChangeRepository :
    BaseRepository<TodoStatusChangesTable, TodoStatusChangePayload>(TodoStatusChangesTable, TodoStatusChangeMapper) {

    suspend fun getAllCommentsForTodo(todoId: UUID): List<TodoStatusChangePayload> {
        return queryForMultipleResults {
            TodoStatusChangesTable.todoId eq todoId
        }
    }
}
