package dev.cryptospace.tasket.server.repository

import dev.cryptospace.tasket.payloads.Payload
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

abstract class BaseRepository<T : Payload>(val table: BaseTable<T>) {
    protected suspend fun <T> suspendedTransaction(block: Transaction.() -> T): T {
        return newSuspendedTransaction(Dispatchers.IO, statement = block)
    }

    protected suspend fun queryForSingleResult(predicate: SqlExpressionBuilder.() -> Op<Boolean>): T? {
        return suspendedTransaction {
            with(table) {
                selectAll().where(predicate).singleOrNull()?.toPayload()
            }
        }
    }

    protected suspend fun queryForMultipleResults(predicate: SqlExpressionBuilder.() -> Op<Boolean>): List<T> {
        return suspendedTransaction {
            with(table) {
                selectAll().where(predicate).map { row -> row.toPayload() }
            }
        }
    }

    suspend fun getAll(): List<T> {
        return suspendedTransaction {
            with(table) {
                selectAll().map { row -> row.toPayload() }
            }
        }
    }

    suspend fun getById(id: UUID): T? {
        return queryForSingleResult { table.id eq id }
    }

    suspend fun insert(payload: T): T {
        return suspendedTransaction {
            with(table) {
                val insertedId =
                    insert { statement ->
                        statement.fromPayload(payload)
                    }[id]
                selectAll().where {
                    id eq insertedId
                }.single().toPayload()
            }
        }
    }

    suspend fun upsert(payload: T, id: UUID): T {
        return suspendedTransaction {
            with(table) {
                val insertedId =
                    upsert { statement ->
                        statement.fromPayload(payload)
                        statement[table.id] = id
                    }[table.id]

                selectAll().where {
                    table.id eq insertedId
                }.single().toPayload()
            }
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
