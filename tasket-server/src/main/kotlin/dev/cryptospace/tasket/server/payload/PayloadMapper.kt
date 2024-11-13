package dev.cryptospace.tasket.server.payload

import dev.cryptospace.tasket.payloads.Payload
import dev.cryptospace.tasket.server.table.BaseTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder

object PayloadMapper {
    fun <T : Payload> mapEntityToPayload(table: BaseTable<T>, resultRow: ResultRow): T {
        val payload = table.payloadCreator()
        table.registeredPayloads.forEach { it.writeToPayload(resultRow, payload) }
        return payload
    }

    fun <T : Payload> mapPayloadToEntity(table: BaseTable<T>, updateBuilder: UpdateBuilder<Int>, payload: T) {
        table.registeredPayloads.forEach { it.readFromPayload(updateBuilder, payload) }
    }
}
