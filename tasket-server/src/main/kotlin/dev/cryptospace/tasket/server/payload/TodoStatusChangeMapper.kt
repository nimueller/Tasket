package dev.cryptospace.tasket.server.payload

import dev.cryptospace.tasket.payloads.TodoStatusChangePayload
import dev.cryptospace.tasket.server.table.TodoStatusChangesTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder

object TodoStatusChangeMapper : PayloadMapper<TodoStatusChangesTable, TodoStatusChangePayload> {
    override fun mapEntityToPayload(table: TodoStatusChangesTable, resultRow: ResultRow): TodoStatusChangePayload {
        val payload = TodoStatusChangePayload(
            oldStatus = resultRow[table.oldStatus].toString(),
            newStatus = resultRow[table.newStatus].toString(),
        )
        payload.updateBaseAttributes(table, resultRow)
        return payload
    }

    override fun mapPayloadToEntity(
        table: TodoStatusChangesTable,
        updateBuilder: UpdateBuilder<Int>,
        payload: TodoStatusChangePayload
    ) {
        // No update of any attributes supported at the moment
    }
}
