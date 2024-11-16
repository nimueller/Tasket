package dev.cryptospace.tasket.server.payload

import dev.cryptospace.tasket.payloads.TodoCommentPayload
import dev.cryptospace.tasket.server.table.TodoCommentsTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder

object TodoCommentMapper : PayloadMapper<TodoCommentsTable, TodoCommentPayload> {
    override fun mapEntityToPayload(table: TodoCommentsTable, resultRow: ResultRow): TodoCommentPayload {
        val payload = TodoCommentPayload(
            comment = resultRow[table.comment],
        )
        payload.updateBaseAttributes(table, resultRow)
        return payload
    }

    override fun mapPayloadAsUpdate(
        table: TodoCommentsTable,
        updateBuilder: UpdateBuilder<Int>,
        payload: TodoCommentPayload,
    ) {
        updateBuilder[table.comment] = payload.comment
    }
}
