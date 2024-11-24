package dev.cryptospace.tasket.server.table.user

import dev.cryptospace.tasket.server.table.BaseTable
import dev.cryptospace.tasket.server.table.postgres.postgresEnum

object UsersTable : BaseTable(name = "tasket.users") {
    val username = text(name = "username")
    val password = text(name = "password")
    val salt = text(name = "salt")
    val role = postgresEnum<UserRole>(name = "user_role", typeName = "user_role")
}
