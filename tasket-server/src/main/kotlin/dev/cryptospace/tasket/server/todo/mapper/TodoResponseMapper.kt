package dev.cryptospace.tasket.server.todo.mapper

import dev.cryptospace.tasket.payloads.todo.response.TodoResponsePayload
import dev.cryptospace.tasket.server.payload.ResponseMapper
import dev.cryptospace.tasket.server.todo.database.TodosTable
import org.jetbrains.exposed.sql.ResultRow

object TodoResponseMapper : ResponseMapper<TodosTable, TodoResponsePayload> {

    override fun mapToPayload(table: TodosTable, row: ResultRow) = TodoResponsePayload(
        metaInformation = mapMetaInformation(table, row),
        label = row[table.label],
        statusId = row[table.status].value.toString(),
        ownerId = row[table.owner].value.toString(),
        sortOrder = row[table.sortOrder],
    )
}
