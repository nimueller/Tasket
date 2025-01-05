package dev.cryptospace.tasket.server.todo.database

import dev.cryptospace.tasket.payloads.todo.response.TodoCommentResponsePayload
import dev.cryptospace.tasket.server.repository.BaseRepository
import dev.cryptospace.tasket.server.todo.mapper.TodoCommentResponseMapper
import java.util.UUID

object TodoCommentRepository :
    BaseRepository<TodoCommentsTable, TodoCommentResponsePayload>(TodoCommentsTable, TodoCommentResponseMapper) {
    suspend fun getAllCommentsForTodo(todoId: UUID): List<TodoCommentResponsePayload> {
        return queryForMultipleResults {
            TodoCommentsTable.todoId eq todoId
        }
    }
}
