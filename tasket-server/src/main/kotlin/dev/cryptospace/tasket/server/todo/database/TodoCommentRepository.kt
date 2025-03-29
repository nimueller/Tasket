package dev.cryptospace.tasket.server.todo.database

import dev.cryptospace.tasket.payloads.todo.response.TodoCommentResponsePayload
import dev.cryptospace.tasket.server.repository.Repository
import dev.cryptospace.tasket.server.todo.mapper.TodoCommentResponseMapper
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

object TodoCommentRepository :
    Repository<TodoCommentsTable, TodoCommentResponsePayload>(TodoCommentsTable, TodoCommentResponseMapper) {
    suspend fun getAllCommentsForTodo(todoId: UUID, owner: UUID): List<TodoCommentResponsePayload> {
        return suspendedTransaction {
            TodoCommentsTable.join(
                onColumn = TodoCommentsTable.todoId,
                otherTable = TodosTable,
                otherColumn = TodosTable.id,
                joinType = JoinType.INNER,
            )
                .selectAll()
                .where {
                    (TodosTable.id eq todoId) and (TodosTable.owner eq owner)
                }.map { row ->
                    responseMapper.mapToPayload(TodoCommentsTable, row)
                }
        }
    }
}
