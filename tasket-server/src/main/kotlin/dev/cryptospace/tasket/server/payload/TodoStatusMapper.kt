package dev.cryptospace.tasket.server.payload

import dev.cryptospace.tasket.payloads.TodoStatusPayload
import dev.cryptospace.tasket.server.table.TodoStatusesTable
import org.jetbrains.exposed.sql.ResultRow

object TodoStatusMapper : ToPayloadMapper<TodoStatusesTable, TodoStatusPayload> {
    override fun mapEntityToPayload(table: TodoStatusesTable, resultRow: ResultRow): TodoStatusPayload {
        val payload = TodoStatusPayload(
            name = resultRow[table.name],
            color = resultRow[table.color],
        )
        payload.updateBaseAttributes(table, resultRow)
        return payload
    }
}
