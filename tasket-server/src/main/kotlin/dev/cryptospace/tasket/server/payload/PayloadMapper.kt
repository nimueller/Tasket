package dev.cryptospace.tasket.server.payload

import dev.cryptospace.tasket.payloads.Payload
import dev.cryptospace.tasket.server.table.BaseTable
import org.jetbrains.exposed.sql.ResultRow

interface PayloadMapper<T : BaseTable, P : Payload> : PayloadToDatabaseMapper<T, P> {
    fun mapEntityToPayload(table: T, resultRow: ResultRow): P

    fun P.updateBaseAttributes(table: T, resultRow: ResultRow) {
        this.id = resultRow[table.id].toString()
        this.createdAt = resultRow[table.createdAt].toString()
        this.updatedAt = resultRow[table.updatedAt].toString()
    }
}
