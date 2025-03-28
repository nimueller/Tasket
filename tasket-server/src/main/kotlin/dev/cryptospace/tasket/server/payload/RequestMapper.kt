package dev.cryptospace.tasket.server.payload

import dev.cryptospace.tasket.payloads.RequestPayload
import dev.cryptospace.tasket.server.table.BaseTable
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import java.util.UUID

fun interface RequestMapper<T : BaseTable, P : RequestPayload> {
    fun mapFromPayload(principal: UUID, table: T, payload: P, updateBuilder: UpdateBuilder<Int>)
}
