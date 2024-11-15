package dev.cryptospace.tasket.server.table

import dev.cryptospace.tasket.types.BootstrapColor

object TodoStatusesTable : BaseTable(name = "todo_statuses") {
    val name = text("name")
    val color = enumeration("color", BootstrapColor::class)
}
