package dev.cryptospace.tasket.server.todo.database

import dev.cryptospace.tasket.payloads.todo.response.TodoResponsePayload
import dev.cryptospace.tasket.server.repository.UserScopedRepository
import dev.cryptospace.tasket.server.todo.mapper.TodoResponseMapper
import org.jetbrains.exposed.sql.SortOrder
import java.util.UUID

object TodoRepository : UserScopedRepository<TodosTable, TodoResponsePayload>(TodosTable, TodoResponseMapper) {
    fun getNextSortOrderIndex(ownerId: UUID): Int = TodosTable.select(TodosTable.sortOrder)
        .where { table.owner eq ownerId }
        .orderBy(TodosTable.sortOrder to SortOrder.DESC)
        .firstOrNull()?.getOrNull(TodosTable.sortOrder)?.plus(1) ?: 0
}
