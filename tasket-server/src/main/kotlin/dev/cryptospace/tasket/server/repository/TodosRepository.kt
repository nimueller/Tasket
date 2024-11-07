package dev.cryptospace.tasket.server.repository

import dev.cryptospace.tasket.payloads.TodoPayload
import dev.cryptospace.tasket.server.table.TodosTable

object TodosRepository : BaseRepository<TodoPayload>(TodosTable)
