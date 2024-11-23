package dev.cryptospace.tasket.server.table

object UsersTable : BaseTable(name = "tasket.users") {
    val username = text("username")
    val password = text("password")
    val salt = text("salt")
}
