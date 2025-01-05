package dev.cryptospace.tasket.server.todo.database

import dev.cryptospace.tasket.server.table.BaseTable
import dev.cryptospace.tasket.server.table.postgres.postgresEnum
import dev.cryptospace.tasket.types.BootstrapColor

object TodoStatusesTable : BaseTable(name = "tasket.todo_statuses") {
    val name = text("name")
    val color = postgresEnum<BootstrapColor>(name = "color", typeName = "bootstrap_color")
}
