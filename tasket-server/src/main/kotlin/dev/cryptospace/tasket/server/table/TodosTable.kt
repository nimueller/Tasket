package dev.cryptospace.tasket.server.table

object TodosTable : BaseTable(name = "tasket.todos") {
    val label = text("label")
    val status = reference("status_id", TodoStatusesTable.id)
}
