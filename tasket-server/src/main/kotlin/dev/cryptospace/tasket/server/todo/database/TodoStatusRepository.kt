package dev.cryptospace.tasket.server.todo.database

import dev.cryptospace.tasket.payloads.todo.response.TodoStatusResponsePayload
import dev.cryptospace.tasket.server.repository.ReadOnlyRepository
import dev.cryptospace.tasket.server.todo.mapper.TodoStatusResponseMapper

object TodoStatusRepository : ReadOnlyRepository<TodoStatusesTable, TodoStatusResponsePayload>(
    TodoStatusesTable,
    TodoStatusResponseMapper,
)
