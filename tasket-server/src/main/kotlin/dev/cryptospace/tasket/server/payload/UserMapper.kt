package dev.cryptospace.tasket.server.payload

import dev.cryptospace.tasket.payloads.UserPayload
import dev.cryptospace.tasket.server.security.Argon2Hashing
import dev.cryptospace.tasket.server.table.UsersTable
import io.ktor.util.encodeBase64
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

        payload.password?.let { password ->
            val salt = Argon2Hashing.generateSalt()
            updateBuilder[table.password] = Argon2Hashing.hashPassword(password, salt).encodeBase64()
            updateBuilder[table.salt] = salt.encodeBase64()
        }
    }
}
