package dev.cryptospace.tasket.server.repository

import dev.cryptospace.tasket.payloads.TodoStatusPayload
import dev.cryptospace.tasket.server.payload.TodoStatusMapper
import dev.cryptospace.tasket.server.table.TodoStatusesTable

object TodoStatusRepository : ReadOnlyRepository<TodoStatusesTable, TodoStatusPayload>(
    TodoStatusesTable,
    TodoStatusMapper,
)
