package dev.cryptospace.tasket.server.payload

import dev.cryptospace.tasket.payloads.TodoPayload
import dev.cryptospace.tasket.server.table.TodosTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder

object TodoMapper : PayloadMapper<TodosTable, TodoPayload> {
    override fun mapEntityToPayload(table: TodosTable, resultRow: ResultRow): TodoPayload {
        val payload = TodoPayload(
            label = resultRow[table.label]
        )
        payload.updateBaseAttributes(table, resultRow)
        return payload
    }

    override fun mapPayloadToEntity(table: TodosTable, updateBuilder: UpdateBuilder<Int>, payload: TodoPayload) {
        updateBuilder[table.label] = payload.label
    }
}
