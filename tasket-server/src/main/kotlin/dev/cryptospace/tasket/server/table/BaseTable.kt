package dev.cryptospace.tasket.server.table

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestampWithTimeZone

abstract class BaseTable(name: String = "") : UUIDTable(name) {
    val createdAt = timestampWithTimeZone(name = "created_at")
    val updatedAt = timestampWithTimeZone(name = "updated_at")
}
