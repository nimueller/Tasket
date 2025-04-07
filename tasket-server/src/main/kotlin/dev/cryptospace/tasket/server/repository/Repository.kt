package dev.cryptospace.tasket.server.repository

import dev.cryptospace.tasket.payloads.ResponsePayload
import dev.cryptospace.tasket.server.payload.ResponseMapper
import dev.cryptospace.tasket.server.table.BaseTable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.deleteReturning
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.upsert
import java.util.UUID

open class Repository<T : BaseTable, RESP : ResponsePayload>(
    val table: T,
    val responseMapper: ResponseMapper<T, RESP>,
) {
    protected suspend fun <R> suspendedTransaction(block: Transaction.() -> R): R {
        return newSuspendedTransaction(Dispatchers.IO, statement = block)
    }

    protected suspend fun queryForSingleResult(predicate: SqlExpressionBuilder.() -> Op<Boolean>): RESP? {
        return suspendedTransaction {
            table.selectAll().where(predicate).singleOrNull()?.let { row ->
                responseMapper.mapToPayload(table, row)
            }
        }
    }

    protected suspend fun queryForMultipleResults(predicate: SqlExpressionBuilder.() -> Op<Boolean>): List<RESP> {
        return suspendedTransaction {
            table.selectAll().where(predicate).map { row ->
                responseMapper.mapToPayload(table, row)
            }
        }
    }

    suspend fun getAllIgnoreOwner(): List<RESP> {
        return queryForMultipleResults { Op.TRUE }
    }

    suspend fun getByIdIgnoreOwner(id: UUID): RESP? {
        return queryForSingleResult { table.id eq id }
    }

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

    suspend fun update(id: UUID, updateBuilder: UpdateBuilder<Int>.() -> Unit = {}): RESP {
        return suspendedTransaction {
            table.update(where = { table.id eq id }) { statement ->
                statement.updateBuilder()
            }

            val row = table.selectAll().where {
                table.id eq id
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
