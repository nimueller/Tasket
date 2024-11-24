package dev.cryptospace.tasket.server.table.user

import dev.cryptospace.tasket.server.table.BaseTable

object UsersTable : BaseTable(name = "tasket.users") {
    val username = text("username")
    val password = text("password")
    val salt = text("salt")
}
