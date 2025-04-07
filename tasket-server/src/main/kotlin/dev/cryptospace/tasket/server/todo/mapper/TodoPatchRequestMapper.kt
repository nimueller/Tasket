package dev.cryptospace.tasket.server.todo.mapper

import dev.cryptospace.tasket.payloads.todo.request.TodoPatchRequestPayload
import dev.cryptospace.tasket.payloads.todo.request.TodoRequestPayload
import dev.cryptospace.tasket.server.payload.PatchRequestMapper
import dev.cryptospace.tasket.server.payload.RequestMapper
import dev.cryptospace.tasket.server.todo.database.TodosTable
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import java.util.UUID

object TodoPatchRequestMapper : PatchRequestMapper<TodosTable, TodoPatchRequestPayload> {
    override fun mapFromPayload(
        principal: UUID,
        table: TodosTable,
        payload: TodoPatchRequestPayload,
        updateBuilder: UpdateBuilder<Int>,
    ) {
        updateBuilder[table.owner] = principal

        payload.label.includeIfPresent(updateBuilder, TodosTable.label)
        payload.statusId.includeForeignKeyIfPresent(updateBuilder, TodosTable.status)
    }
}
