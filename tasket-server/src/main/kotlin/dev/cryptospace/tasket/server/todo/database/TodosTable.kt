package dev.cryptospace.tasket.server.todo.database

import dev.cryptospace.tasket.server.table.BaseTable
import dev.cryptospace.tasket.server.user.database.UsersTable

object TodosTable : BaseTable(name = "tasket.todos") {
    val label = text("label")
    val status = reference("status_id", TodoStatusesTable.id)
    val owner = reference("owner_id", UsersTable.id)
}
