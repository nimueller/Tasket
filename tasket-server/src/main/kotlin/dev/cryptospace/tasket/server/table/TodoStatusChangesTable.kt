package dev.cryptospace.tasket.server.table

object TodoStatusChangesTable : BaseTable(name = "tasket.todo_status_changes") {
    val todoId = reference("todo_id", TodosTable)
    val oldStatus = uuid("old_status_id")
    val newStatus = uuid("new_status_id")
}
