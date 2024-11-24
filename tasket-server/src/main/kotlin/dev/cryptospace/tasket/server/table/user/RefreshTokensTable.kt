package dev.cryptospace.tasket.server.table.user

import dev.cryptospace.tasket.server.table.BaseTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestampWithTimeZone

object RefreshTokensTable : BaseTable("tasket.user_refresh_tokens") {
    val userId = reference("user_id", UsersTable)
    val token = text("token")
    val expiration = timestampWithTimeZone("expiration")
}
