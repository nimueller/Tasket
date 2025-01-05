package dev.cryptospace.tasket.server.todo.mapper

import dev.cryptospace.tasket.payloads.todo.request.TodoCommentRequestPayload
import dev.cryptospace.tasket.server.payload.RequestMapper
import dev.cryptospace.tasket.server.todo.database.TodoCommentsTable
import org.jetbrains.exposed.sql.statements.UpdateBuilder

object TodoCommentRequestMapper : RequestMapper<TodoCommentsTable, TodoCommentRequestPayload> {
    override fun mapFromPayload(
        table: TodoCommentsTable,
        payload: TodoCommentRequestPayload,
        updateBuilder: UpdateBuilder<Int>,
    ) {
        updateBuilder[table.comment] = payload.comment
    }
}
