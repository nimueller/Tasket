package dev.cryptospace.tasket.server.repository

import dev.cryptospace.tasket.payloads.ResponsePayload
import dev.cryptospace.tasket.server.payload.ResponseMapper
import dev.cryptospace.tasket.server.table.BaseTable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID

abstract class ReadOnlyRepository<T : BaseTable, P : ResponsePayload>(
    private val table: T,
    private val responseMapper: ResponseMapper<T, P>,
) {
    protected suspend fun <R> suspendedTransaction(block: Transaction.() -> R): R {
        return newSuspendedTransaction(Dispatchers.IO, statement = block)
    }

    private suspend fun queryForSingleResult(predicate: SqlExpressionBuilder.() -> Op<Boolean>): P? {
        return suspendedTransaction {
            table.selectAll().where(predicate).singleOrNull()?.let { row ->
                responseMapper.mapToPayload(table, row)
            }
        }
    }

    protected suspend fun queryForMultipleResults(predicate: SqlExpressionBuilder.() -> Op<Boolean>): List<P> {
        return suspendedTransaction {
            table.selectAll().where(predicate).map { row ->
                responseMapper.mapToPayload(table, row)
            }
        }
    }

    suspend fun getAll(): List<P> {
        return suspendedTransaction {
            table.selectAll().map { row ->
                responseMapper.mapToPayload(table, row)
            }
        }
    }

    suspend fun getById(id: UUID): P? {
        return queryForSingleResult { table.id eq id }
    }
}
