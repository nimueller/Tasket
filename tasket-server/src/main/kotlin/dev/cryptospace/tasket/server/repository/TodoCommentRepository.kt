package dev.cryptospace.tasket.server.repository

import dev.cryptospace.tasket.payloads.TodoCommentPayload
import dev.cryptospace.tasket.server.payload.TodoCommentMapper
import dev.cryptospace.tasket.server.table.TodoCommentsTable
import java.util.UUID

object TodoCommentRepository :
    BaseRepository<TodoCommentsTable, TodoCommentPayload>(TodoCommentsTable, TodoCommentMapper) {
    suspend fun getAllCommentsForTodo(todoId: UUID): List<TodoCommentPayload> {
        return queryForMultipleResults {
            TodoCommentsTable.id eq todoId
        }
    }
}
