package dev.cryptospace.tasket.server.repository

import dev.cryptospace.tasket.payloads.UserPayload
import dev.cryptospace.tasket.server.payload.UserMapper
import dev.cryptospace.tasket.server.table.UsersTable
import org.jetbrains.exposed.sql.selectAll

object UserRepository : BaseRepository<UsersTable, UserPayload>(UsersTable, UserMapper) {
    suspend fun findByUsername(username: String): UserPayload? {
        return suspendedTransaction {
            val row = UsersTable.selectAll().where {
                UsersTable.username eq username
            }.singleOrNull()

            row?.let { UserMapper.mapEntityToPayload(UsersTable, it) }
        }
    }
}
