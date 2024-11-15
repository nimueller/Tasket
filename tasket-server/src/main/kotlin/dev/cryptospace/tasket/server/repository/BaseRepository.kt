package dev.cryptospace.tasket.server.repository

import dev.cryptospace.tasket.payloads.Payload
import dev.cryptospace.tasket.server.payload.PayloadMapper
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

abstract class BaseRepository<T : BaseTable, V : Payload>(
    private val table: T,
    private val mapper: PayloadMapper<T, V>
) {
    private suspend fun <R> suspendedTransaction(block: Transaction.() -> R): R {
        return newSuspendedTransaction(Dispatchers.IO, statement = block)
    }

    private suspend fun queryForSingleResult(predicate: SqlExpressionBuilder.() -> Op<Boolean>): V? {
        return suspendedTransaction {
            table.selectAll().where(predicate).singleOrNull()?.let { row ->
                mapper.mapEntityToPayload(table, row)
            }
        }
    }

    protected suspend fun queryForMultipleResults(predicate: SqlExpressionBuilder.() -> Op<Boolean>): List<V> {
        return suspendedTransaction {
            table.selectAll().where(predicate).map { row ->
                mapper.mapEntityToPayload(table, row)
            }
        }
    }

    suspend fun getAll(): List<V> {
        return suspendedTransaction {
            table.selectAll().map { row ->
                mapper.mapEntityToPayload(table, row)
            }
        }
    }

    suspend fun getById(id: UUID): V? {
        return queryForSingleResult { table.id eq id }
    }

    suspend fun insert(payload: V): V {
        return suspendedTransaction {
            val insertedId = table.insert { statement ->
                mapper.mapPayloadToEntity(table, statement, payload)
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
