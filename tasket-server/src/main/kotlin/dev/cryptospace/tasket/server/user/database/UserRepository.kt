package dev.cryptospace.tasket.server.user.database

import dev.cryptospace.tasket.payloads.user.response.UserResponsePayload
import dev.cryptospace.tasket.server.repository.Repository
import dev.cryptospace.tasket.server.user.mapper.UserResponseMapper
import java.util.UUID

object UserRepository : Repository<UsersTable, UserResponsePayload>(UsersTable, UserResponseMapper) {
    suspend fun changePassword(id: UUID, password: String): UserResponsePayload {
        return upsert(id) {
            this[UsersTable.password] = password
        }
    }

    suspend fun getUserRole(id: UUID): UserRole {
        return suspendedTransaction {
            UsersTable.select(UsersTable.role).where { UsersTable.id eq id }.single()[UsersTable.role]
        }
    }
}
