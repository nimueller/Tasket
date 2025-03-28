package dev.cryptospace.tasket.server.repository

import dev.cryptospace.tasket.payloads.ResponsePayload
import dev.cryptospace.tasket.server.payload.ResponseMapper
import dev.cryptospace.tasket.server.table.OwnedTable
import org.jetbrains.exposed.sql.and
import java.util.UUID

open class UserScopedRepository<T : OwnedTable, RESP : ResponsePayload>(
    table: T,
    responseMapper: ResponseMapper<T, RESP>,
) : Repository<T, RESP>(table, responseMapper) {
    suspend fun getAllOwnedBy(ownerId: UUID): List<RESP> {
        return queryForMultipleResults { table.owner eq ownerId }
    }

    suspend fun getByIdAndOwner(id: UUID, ownerId: UUID): RESP? {
        return queryForSingleResult { (table.id eq id) and (table.owner eq ownerId) }
    }
}
