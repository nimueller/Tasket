package dev.cryptospace.tasket.server.todo.mapper

import dev.cryptospace.tasket.payloads.todo.request.TodoRequestPayload
import dev.cryptospace.tasket.server.payload.RequestMapper
import dev.cryptospace.tasket.server.todo.database.TodosTable
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import java.util.UUID

object TodoRequestMapper : RequestMapper<TodosTable, TodoRequestPayload> {
    override fun mapFromPayload(table: TodosTable, payload: TodoRequestPayload, updateBuilder: UpdateBuilder<Int>) {
        updateBuilder[table.label] = payload.label

        payload.statusId?.let {
            updateBuilder[table.status] = UUID.fromString(it)
        }
    }
}
