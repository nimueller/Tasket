package dev.cryptospace.tasket.server.repository

import dev.cryptospace.tasket.payloads.Payload
import dev.cryptospace.tasket.server.payload.PayloadMapper
import dev.cryptospace.tasket.server.payload.PayloadToDatabaseMapper
import dev.cryptospace.tasket.server.table.BaseTable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.deleteReturning
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.upsert
import java.util.UUID

abstract class BaseRepository<T : BaseTable, P : Payload>(
    private val table: T,
    private val mapper: PayloadMapper<T, P>,
) {
    private suspend fun <R> suspendedTransaction(block: Transaction.() -> R): R {
        return newSuspendedTransaction(Dispatchers.IO, statement = block)
    }

    private suspend fun queryForSingleResult(predicate: SqlExpressionBuilder.() -> Op<Boolean>): P? {
        return suspendedTransaction {
            table.selectAll().where(predicate).singleOrNull()?.let { row ->
                mapper.mapEntityToPayload(table, row)
            }
        }
    }

    protected suspend fun queryForMultipleResults(predicate: SqlExpressionBuilder.() -> Op<Boolean>): List<P> {
        return suspendedTransaction {
            table.selectAll().where(predicate).map { row ->
                mapper.mapEntityToPayload(table, row)
            }
        }
    }

    suspend fun getAll(): List<P> {
        return suspendedTransaction {
            table.selectAll().map { row ->
                mapper.mapEntityToPayload(table, row)
            }
        }
    }

    suspend fun getById(id: UUID): P? {
        return queryForSingleResult { table.id eq id }
    }

    suspend fun insert(payload: P): P {
        return suspendedTransaction {
            val insertedId = table.insert { statement ->
                mapper.mapPayloadAsUpdate(table, statement, payload)
            }[table.id]

            val row = table.selectAll().where {
                table.id eq insertedId
            }.single()

            mapper.mapEntityToPayload(table, row)
        }
    }

    suspend fun upsert(payload: P, id: UUID): P {
        return patch(payload, id, mapper)
    }

    suspend fun <R : Payload> patch(patchPayload: R, id: UUID, patchPayloadMapper: PayloadToDatabaseMapper<T, R>): P {
        return suspendedTransaction {
            val updatedId = table.upsert(where = { table.id eq id }) { statement ->
                patchPayloadMapper.mapPayloadAsUpdate(table, statement, patchPayload)
            }[table.id]

            val row = table.selectAll().where {
                table.id eq updatedId
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
