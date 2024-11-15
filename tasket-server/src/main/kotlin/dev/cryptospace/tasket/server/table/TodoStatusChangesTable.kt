package dev.cryptospace.tasket.server.table

object TodoStatusChangesTable : BaseTable(name = "todo_status_changes") {
    val oldStatus = uuid("old_status_id")
    val newStatus = uuid("new_status_id")
}
