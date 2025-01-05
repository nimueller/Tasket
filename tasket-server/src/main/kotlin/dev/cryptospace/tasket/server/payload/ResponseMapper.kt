package dev.cryptospace.tasket.server.payload

import dev.cryptospace.tasket.payloads.MetaInformationPayload
import dev.cryptospace.tasket.payloads.ResponsePayload
import dev.cryptospace.tasket.server.table.BaseTable
import org.jetbrains.exposed.sql.ResultRow

interface ResponseMapper<T : BaseTable, P : ResponsePayload> {
    fun mapToPayload(table: T, row: ResultRow): P

    fun mapMetaInformation(table: T, row: ResultRow): MetaInformationPayload {
        return MetaInformationPayload(
            id = row[table.id].toString(),
            createdAt = row[table.createdAt].toString(),
            updatedAt = row[table.updatedAt].toString(),
        )
    }
}
