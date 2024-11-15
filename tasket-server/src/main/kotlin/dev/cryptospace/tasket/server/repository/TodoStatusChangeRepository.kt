package dev.cryptospace.tasket.server.repository

import dev.cryptospace.tasket.payloads.TodoStatusChangePayload
import dev.cryptospace.tasket.server.payload.TodoStatusChangeMapper
import dev.cryptospace.tasket.server.table.TodoStatusChangesTable

object TodoStatusChangeRepository :
    BaseRepository<TodoStatusChangesTable, TodoStatusChangePayload>(TodoStatusChangesTable, TodoStatusChangeMapper)
