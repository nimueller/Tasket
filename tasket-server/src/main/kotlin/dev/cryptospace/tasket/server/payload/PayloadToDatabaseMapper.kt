package dev.cryptospace.tasket.server.payload

import dev.cryptospace.tasket.payloads.Payload
import dev.cryptospace.tasket.server.table.BaseTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.statements.UpdateBuilder

fun interface PayloadToDatabaseMapper<T : BaseTable, P : Payload> {
    fun mapPayloadAsUpdate(table: T, updateBuilder: UpdateBuilder<Int>, payload: P)

    fun <V> UpdateBuilder<Int>.includeInUpdateIfNonNull(column: Column<V>, value: V?) {
        if (value != null) {
            this[column] = value
        }
    }
}
