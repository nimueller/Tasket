package dev.cryptospace.tasket.server.todo.database

import dev.cryptospace.tasket.payloads.todo.response.TodoResponsePayload
import dev.cryptospace.tasket.server.repository.UserScopedRepository
import dev.cryptospace.tasket.server.todo.mapper.TodoResponseMapper

object TodoRepository : UserScopedRepository<TodosTable, TodoResponsePayload>(TodosTable, TodoResponseMapper)
