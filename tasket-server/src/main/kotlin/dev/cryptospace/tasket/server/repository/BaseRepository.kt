package dev.cryptospace.tasket.server.repository

import dev.cryptospace.tasket.payloads.ResponsePayload
import dev.cryptospace.tasket.server.payload.ResponseMapper
import dev.cryptospace.tasket.server.table.BaseTable
import org.jetbrains.exposed.sql.deleteReturning
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.upsert
import java.util.UUID

abstract class BaseRepository<T : BaseTable, RESP : ResponsePayload>(
    val table: T,
    private val responseMapper: ResponseMapper<T, RESP>,
) : ReadOnlyRepository<T, RESP>(table, responseMapper) {
    suspend fun insert(updateBuilder: UpdateBuilder<Int>.() -> Unit = {}): RESP {
        return suspendedTransaction {
            val insertedId = table.insert { statement ->
                statement.updateBuilder()
            }[table.id]

            val row = table.selectAll().where {
                table.id eq insertedId
            }.single()

            responseMapper.mapToPayload(table, row)
        }
    }

    suspend fun upsert(id: UUID, updateBuilder: UpdateBuilder<Int>.() -> Unit = {}): RESP {
        return suspendedTransaction {
            val insertedId = table.upsert(where = { table.id eq id }) { statement ->
                statement.updateBuilder()
                statement[table.id] = id
            }[table.id]

            val row = table.selectAll().where {
                table.id eq insertedId
            }.single()

            responseMapper.mapToPayload(table, row)
        }
    }

    suspend fun delete(id: UUID): Int {
        return suspendedTransaction {
            with(table) {
                deleteReturning {
                    table.id eq id
                }.count()
            }
        }
    }
}
