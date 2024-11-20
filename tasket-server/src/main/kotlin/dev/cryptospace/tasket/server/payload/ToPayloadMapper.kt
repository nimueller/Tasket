package dev.cryptospace.tasket.server.payload

import dev.cryptospace.tasket.payloads.Payload
import dev.cryptospace.tasket.server.table.BaseTable
import org.jetbrains.exposed.sql.ResultRow

interface ToPayloadMapper<T : BaseTable, V : Payload> {
    fun mapEntityToPayload(table: T, resultRow: ResultRow): V

    fun V.updateBaseAttributes(table: T, resultRow: ResultRow) {
        this.id = resultRow[table.id].toString()
        this.createdAt = resultRow[table.createdAt].toString()
        this.updatedAt = resultRow[table.updatedAt].toString()
    }
}
