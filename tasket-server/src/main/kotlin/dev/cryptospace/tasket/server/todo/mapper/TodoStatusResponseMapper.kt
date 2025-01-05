package dev.cryptospace.tasket.server.todo.mapper

import dev.cryptospace.tasket.payloads.todo.response.TodoStatusResponsePayload
import dev.cryptospace.tasket.server.payload.ResponseMapper
import dev.cryptospace.tasket.server.todo.database.TodoStatusesTable
import org.jetbrains.exposed.sql.ResultRow

object TodoStatusResponseMapper : ResponseMapper<TodoStatusesTable, TodoStatusResponsePayload> {
    override fun mapToPayload(table: TodoStatusesTable, row: ResultRow): TodoStatusResponsePayload {
        return TodoStatusResponsePayload(
            metaInformation = mapMetaInformation(table, row),
            name = row[table.name],
            color = row[table.color],
        )
    }
}
