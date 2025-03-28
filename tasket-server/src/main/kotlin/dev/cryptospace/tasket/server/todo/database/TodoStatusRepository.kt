package dev.cryptospace.tasket.server.todo.database

import dev.cryptospace.tasket.payloads.todo.response.TodoStatusResponsePayload
import dev.cryptospace.tasket.server.repository.Repository
import dev.cryptospace.tasket.server.todo.mapper.TodoStatusResponseMapper

object TodoStatusRepository : Repository<TodoStatusesTable, TodoStatusResponsePayload>(
    TodoStatusesTable,
    TodoStatusResponseMapper,
)
