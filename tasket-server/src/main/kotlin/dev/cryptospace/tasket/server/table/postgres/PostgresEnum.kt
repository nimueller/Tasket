package dev.cryptospace.tasket.server.table.postgres

import dev.cryptospace.tasket.server.table.BaseTable
import org.jetbrains.exposed.sql.Column

inline fun <reified T : Enum<T>> BaseTable.postgresEnum(name: String, typeName: String): Column<T> {
    return customEnumeration(
        name = name,
        sql = typeName,
        fromDb = { value -> enumValueOf(value as String) },
        toDb = { value -> PGEnum(typeName, value) },
    )
}
