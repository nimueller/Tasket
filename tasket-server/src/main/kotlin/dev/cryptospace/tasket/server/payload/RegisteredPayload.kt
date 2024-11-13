package dev.cryptospace.tasket.server.payload

import dev.cryptospace.tasket.payloads.Payload
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import kotlin.reflect.KMutableProperty1

data class RegisteredPayload<T, R : Payload, M>(
    val column: Column<T>,
    val property: KMutableProperty1<in R, M>,
    val columnToPayloadType: (T) -> M,
    val payloadToColumnType: (M) -> T?,
) {
    fun writeToPayload(resultRow: ResultRow, payload: R) {
        property.set(payload, columnToPayloadType(resultRow[column]))
    }

    fun readFromPayload(updateBuilder: UpdateBuilder<Int>, payload: R) {
        val value = property.get(payload)?.let(payloadToColumnType)
        if (value != null) {
            updateBuilder[column] = value
        }
    }
}
