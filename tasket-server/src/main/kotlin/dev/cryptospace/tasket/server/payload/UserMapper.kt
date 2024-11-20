package dev.cryptospace.tasket.server.payload

import dev.cryptospace.tasket.payloads.UserPayload
import dev.cryptospace.tasket.server.table.UsersTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder

object UserMapper : PayloadMapper<UsersTable, UserPayload> {
    override fun mapEntityToPayload(table: UsersTable, resultRow: ResultRow): UserPayload {
        val payload = UserPayload(
            username = resultRow[table.username],
            password = null,
        )
        payload.updateBaseAttributes(table, resultRow)
        return payload
    }

    override fun mapPayloadToEntity(table: UsersTable, updateBuilder: UpdateBuilder<Int>, payload: UserPayload) {
        updateBuilder[table.username] = payload.username
    }
}
