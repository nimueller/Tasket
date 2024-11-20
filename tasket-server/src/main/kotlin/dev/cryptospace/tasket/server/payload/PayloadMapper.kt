package dev.cryptospace.tasket.server.payload

import dev.cryptospace.tasket.payloads.Payload
import dev.cryptospace.tasket.server.table.BaseTable
import org.jetbrains.exposed.sql.statements.UpdateBuilder

interface PayloadMapper<T : BaseTable, V : Payload> : ToPayloadMapper<T, V> {
    fun mapPayloadToEntity(table: T, updateBuilder: UpdateBuilder<Int>, payload: V)
}
