package dev.cryptospace.tasket.server.table

import dev.cryptospace.tasket.payloads.Payload
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder

interface PayloadMapper<T : Payload> {
    fun ResultRow.toPayload(): T

    fun UpdateBuilder<Int>.fromPayload(payload: T)
}
