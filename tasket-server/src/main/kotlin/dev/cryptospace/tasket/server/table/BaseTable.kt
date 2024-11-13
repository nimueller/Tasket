package dev.cryptospace.tasket.server.table

import dev.cryptospace.tasket.payloads.Payload
import dev.cryptospace.tasket.server.payload.RegisteredPayload
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.kotlin.datetime.timestampWithTimeZone
import java.time.OffsetDateTime
import java.util.UUID
import kotlin.reflect.KMutableProperty1

abstract class BaseTable<T : Payload>(name: String = "", val payloadCreator: () -> T) : UUIDTable(name) {

    private val createdAt = timestampWithTimeZone(name = "created_at")
    private val updatedAt = timestampWithTimeZone(name = "updated_at")

    private val internalRegisteredPayloads = mutableListOf<RegisteredPayload<*, T, *>>()
    val registeredPayloads: List<RegisteredPayload<*, T, *>>
        get() = internalRegisteredPayloads

    protected fun <R, V> registerPayload(
        column: Column<R>,
        property: KMutableProperty1<in T, V>,
        columnToPayloadType: (R) -> V,
        payloadToColumnType: (V) -> R?,
    ) {
        internalRegisteredPayloads += RegisteredPayload(
            column,
            property,
            columnToPayloadType,
            payloadToColumnType,
        )
    }

    protected fun registerStringPayload(column: Column<String>, property: KMutableProperty1<in T, String>) {
        registerPayload(
            column = column,
            property = property,
            columnToPayloadType = { it },
            payloadToColumnType = { it },
        )
    }

    protected fun registerTimestampPayload(
        column: Column<OffsetDateTime>,
        property: KMutableProperty1<in T, String?>,
    ) {
        registerPayload(
            column = column,
            property = property,
            columnToPayloadType = { it.toString() },
            payloadToColumnType = {
                return@registerPayload if (it != null) {
                    OffsetDateTime.parse(it)
                } else {
                    null
                }
            },
        )
    }

    protected fun registerUuidPayload(column: Column<UUID>, property: KMutableProperty1<in T, String?>) {
        registerPayload(
            column = column,
            property = property,
            columnToPayloadType = { it.toString() },
            payloadToColumnType = { UUID.fromString(it) },
        )
    }

    init {
        registerPayload(id, Payload::id, { it.value.toString() }, { null })
        registerTimestampPayload(createdAt, Payload::createdAt)
        registerTimestampPayload(updatedAt, Payload::updatedAt)
    }
}
