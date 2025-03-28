package dev.cryptospace.tasket.server.todo.database

import dev.cryptospace.tasket.server.table.OwnedTable

object TodosTable : OwnedTable(name = "tasket.todos") {
    val label = text("label")
    val status = reference("status_id", TodoStatusesTable.id)
}
