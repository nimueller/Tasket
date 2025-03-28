package dev.cryptospace.tasket.server.table

import dev.cryptospace.tasket.server.user.database.UsersTable

abstract class OwnedTable(name: String = "") : BaseTable(name) {
    val owner = reference("owner_id", UsersTable)
}
