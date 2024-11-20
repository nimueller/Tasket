package dev.cryptospace.tasket.server.repository

import dev.cryptospace.tasket.payloads.Payload
import dev.cryptospace.tasket.server.payload.ToPayloadMapper
import dev.cryptospace.tasket.server.table.BaseTable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID

abstract class ReadOnlyRepository<T : BaseTable, V : Payload>(
    private val table: T,
    private val mapper: ToPayloadMapper<T, V>,
) {
    protected suspend fun <R> suspendedTransaction(block: Transaction.() -> R): R {
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
}
