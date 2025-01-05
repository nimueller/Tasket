package dev.cryptospace.tasket.server.todo.database

import dev.cryptospace.tasket.server.table.BaseTable

object TodoStatusChangesTable : BaseTable(name = "tasket.todo_status_changes") {
    val todoId = reference("todo_id", TodosTable)
    val oldStatus = uuid("old_status_id")
    val newStatus = uuid("new_status_id")
}
