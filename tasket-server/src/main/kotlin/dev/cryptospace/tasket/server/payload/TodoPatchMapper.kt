package dev.cryptospace.tasket.server.payload

import dev.cryptospace.tasket.payloads.TodoPatchPayload
import dev.cryptospace.tasket.server.table.TodosTable
import org.jetbrains.exposed.sql.statements.UpdateBuilder

object TodoPatchMapper : PayloadToDatabaseMapper<TodosTable, TodoPatchPayload> {
    override fun mapPayloadAsUpdate(table: TodosTable, updateBuilder: UpdateBuilder<Int>, payload: TodoPatchPayload) {
        updateBuilder.includeInUpdateIfNonNull(table.label, payload.label)
    }
}
