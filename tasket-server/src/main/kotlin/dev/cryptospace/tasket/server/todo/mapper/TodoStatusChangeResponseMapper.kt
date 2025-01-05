package dev.cryptospace.tasket.server.todo.mapper

import dev.cryptospace.tasket.payloads.todo.response.TodoStatusChangeResponsePayload
import dev.cryptospace.tasket.server.payload.ResponseMapper
import dev.cryptospace.tasket.server.todo.database.TodoStatusChangesTable
import org.jetbrains.exposed.sql.ResultRow

object TodoStatusChangeResponseMapper : ResponseMapper<TodoStatusChangesTable, TodoStatusChangeResponsePayload> {
    override fun mapToPayload(table: TodoStatusChangesTable, row: ResultRow): TodoStatusChangeResponsePayload {
        return TodoStatusChangeResponsePayload(
            metaInformation = mapMetaInformation(table, row),
            oldStatus = row[table.oldStatus].toString(),
            newStatus = row[table.newStatus].toString(),
        )
    }
}
