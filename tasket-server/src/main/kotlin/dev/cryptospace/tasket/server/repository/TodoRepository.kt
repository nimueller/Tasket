package dev.cryptospace.tasket.server.repository

import dev.cryptospace.tasket.payloads.TodoPayload
import dev.cryptospace.tasket.server.payload.TodoMapper
import dev.cryptospace.tasket.server.table.TodosTable

object TodoRepository : BaseRepository<TodosTable, TodoPayload>(TodosTable, TodoMapper)
