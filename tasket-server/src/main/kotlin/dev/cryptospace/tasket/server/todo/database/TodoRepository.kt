package dev.cryptospace.tasket.server.todo.database

import dev.cryptospace.tasket.payloads.todo.response.TodoResponsePayload
import dev.cryptospace.tasket.server.repository.BaseRepository
import dev.cryptospace.tasket.server.todo.mapper.TodoResponseMapper

object TodoRepository : BaseRepository<TodosTable, TodoResponsePayload>(TodosTable, TodoResponseMapper)
