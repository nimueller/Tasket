package dev.cryptospace.tasket.server.user.mapper

import dev.cryptospace.tasket.payloads.user.request.UserRequestPayload
import dev.cryptospace.tasket.server.payload.RequestMapper
import dev.cryptospace.tasket.server.security.Argon2Hashing
import dev.cryptospace.tasket.server.user.database.UsersTable
import io.ktor.util.encodeBase64
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import java.util.UUID

object UserRequestMapper : RequestMapper<UsersTable, UserRequestPayload> {
    override fun mapFromPayload(
        principal: UUID,
        table: UsersTable,
        payload: UserRequestPayload,
        updateBuilder: UpdateBuilder<Int>
    ) {
        updateBuilder[table.username] = payload.username

        val password = payload.password
        val salt = Argon2Hashing.generateSalt()
        updateBuilder[table.password] = Argon2Hashing.hashPassword(password, salt).encodeBase64()
        updateBuilder[table.salt] = salt.encodeBase64()
    }
}
