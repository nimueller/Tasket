package dev.cryptospace.tasket.server.todo.mapper

import dev.cryptospace.tasket.payloads.todo.response.TodoCommentResponsePayload
import dev.cryptospace.tasket.server.payload.ResponseMapper
import dev.cryptospace.tasket.server.todo.database.TodoCommentsTable
import org.jetbrains.exposed.sql.ResultRow

object TodoCommentResponseMapper : ResponseMapper<TodoCommentsTable, TodoCommentResponsePayload> {

    override fun mapToPayload(table: TodoCommentsTable, row: ResultRow): TodoCommentResponsePayload {
        return TodoCommentResponsePayload(
            metaInformation = mapMetaInformation(table, row),
            comment = row[table.comment],
        )
    }
}
