package dev.cryptospace.tasket.server.user.database

import dev.cryptospace.tasket.payloads.user.response.UserResponsePayload
import dev.cryptospace.tasket.server.repository.Repository
import dev.cryptospace.tasket.server.table.user.UserId
import dev.cryptospace.tasket.server.user.mapper.UserResponseMapper
import io.ktor.util.encodeBase64
import java.util.UUID

object UserRepository : Repository<UsersTable, UserResponsePayload>(UsersTable, UserResponseMapper) {
    suspend fun changePassword(id: UserId, password: ByteArray, salt: ByteArray): UserResponsePayload {
        return update(id.value) {
            this[UsersTable.password] = password.encodeBase64()
            this[UsersTable.salt] = salt.encodeBase64()
        }
    }

    suspend fun getUserRole(id: UUID): UserRole {
        return suspendedTransaction {
            UsersTable.select(UsersTable.role).where { UsersTable.id eq id }.single()[UsersTable.role]
        }
    }
}
