package dev.cryptospace.tasket.server.table

import dev.cryptospace.tasket.payloads.Payload
import dev.cryptospace.tasket.server.utils.toIso8601String
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.kotlin.datetime.timestampWithTimeZone

abstract class BaseTable<T : Payload>(name: String = "") : UUIDTable(name), PayloadMapper<T> {
    val createdAt = timestampWithTimeZone(name = "created_at")
    val updatedAt = timestampWithTimeZone(name = "updated_at")

    protected fun ResultRow.writeBaseColumnsToPayload(payload: T) {
        payload.createdAt = this[createdAt].toIso8601String()
        payload.updatedAt = this[updatedAt].toIso8601String()
    }
}
