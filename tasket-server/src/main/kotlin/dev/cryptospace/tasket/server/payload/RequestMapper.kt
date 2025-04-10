package dev.cryptospace.tasket.server.payload

import dev.cryptospace.tasket.payloads.OptionalField
import dev.cryptospace.tasket.payloads.RequestPayload
import dev.cryptospace.tasket.server.table.BaseTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import java.util.UUID

fun interface RequestMapper<T : BaseTable, P : RequestPayload> {
    fun mapFromPayload(principal: UUID, table: T, payload: P, updateBuilder: UpdateBuilder<Int>)

    fun OptionalField<String>.includeForeignKeyIfPresent(
        updateBuilder: UpdateBuilder<Int>,
        column: Column<EntityID<UUID>>,
    ) {
        includeIfPresent { value ->
            updateBuilder[column] = UUID.fromString(value)
        }
    }

    fun <F> OptionalField<F>.includeIfPresent(updateBuilder: UpdateBuilder<Int>, column: Column<F>) {
        includeIfPresent { value ->
            updateBuilder[column] = value
        }
    }

    fun <F> OptionalField<F>.includeIfPresent(update: (F) -> Unit) {
        when (this) {
            is OptionalField.Present -> update(value)
            is OptionalField.Missing -> {
                // No action needed
            }
        }
    }
}
