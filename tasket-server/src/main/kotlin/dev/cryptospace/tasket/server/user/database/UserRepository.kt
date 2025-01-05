package dev.cryptospace.tasket.server.user.database

import dev.cryptospace.tasket.payloads.user.response.UserResponsePayload
import dev.cryptospace.tasket.server.repository.BaseRepository
import dev.cryptospace.tasket.server.user.mapper.UserResponseMapper
import java.util.UUID

object UserRepository : BaseRepository<UsersTable, UserResponsePayload>(UsersTable, UserResponseMapper) {
    suspend fun changePassword(id: UUID, password: String): UserResponsePayload {
        return upsert(id) {
            this[UsersTable.password] = password
        }
    }
}
