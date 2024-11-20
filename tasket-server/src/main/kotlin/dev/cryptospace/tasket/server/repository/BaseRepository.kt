package dev.cryptospace.tasket.server.repository

import dev.cryptospace.tasket.payloads.Payload
import dev.cryptospace.tasket.server.payload.PayloadMapper
import dev.cryptospace.tasket.server.table.BaseTable
import org.jetbrains.exposed.sql.deleteReturning
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.upsert
import java.util.UUID

abstract class BaseRepository<T : BaseTable, V : Payload>(
    private val table: T,
    private val mapper: PayloadMapper<T, V>,
) : ReadOnlyRepository<T, V>(table, mapper) {
    suspend fun insert(payload: V, includeAdditionalAttributes: UpdateBuilder<Int>.() -> Unit = {}): V {
        return suspendedTransaction {
            val insertedId = table.insert { statement ->
                mapper.mapPayloadToEntity(table, statement, payload)
                statement.includeAdditionalAttributes()
            }[table.id]

            val row = table.selectAll().where {
                table.id eq insertedId
            }.single()

            mapper.mapEntityToPayload(table, row)
        }
    }

    suspend fun upsert(payload: V, id: UUID): V {
        return suspendedTransaction {
            val insertedId = table.upsert { statement ->
                mapper.mapPayloadToEntity(table, statement, payload)
                statement[table.id] = id
            }[table.id]

            val row = table.selectAll().where {
                table.id eq insertedId
            }.single()

            mapper.mapEntityToPayload(table, row)
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
