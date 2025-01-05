package dev.cryptospace.tasket.server.user.mapper

import dev.cryptospace.tasket.payloads.user.response.UserResponsePayload
import dev.cryptospace.tasket.server.payload.ResponseMapper
import dev.cryptospace.tasket.server.user.database.UsersTable
import org.jetbrains.exposed.sql.ResultRow

object UserResponseMapper : ResponseMapper<UsersTable, UserResponsePayload> {
    override fun mapToPayload(table: UsersTable, row: ResultRow): UserResponsePayload {
        return UserResponsePayload(
            metaInformation = mapMetaInformation(table, row),
            username = row[table.username],
        )
    }
}
