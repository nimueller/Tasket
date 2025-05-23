package dev.cryptospace.tasket.server.user.mapper

import dev.cryptospace.tasket.payloads.user.request.UserChangePasswordRequestPayload
import dev.cryptospace.tasket.server.payload.RequestMapper
import dev.cryptospace.tasket.server.security.Argon2Hashing
import dev.cryptospace.tasket.server.user.database.UsersTable
import io.ktor.util.encodeBase64
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import java.util.UUID

class UserChangePasswordMapper : RequestMapper<UsersTable, UserChangePasswordRequestPayload> {
    override fun mapFromPayload(
        principal: UUID,
        table: UsersTable,
        payload: UserChangePasswordRequestPayload,
        updateBuilder: UpdateBuilder<Int>,
    ) {
        val password = payload.newPassword
        val salt = Argon2Hashing.generateSalt()
        updateBuilder[table.password] = Argon2Hashing.hashPassword(password, salt).encodeBase64()
        updateBuilder[table.salt] = salt.encodeBase64()
    }
}
