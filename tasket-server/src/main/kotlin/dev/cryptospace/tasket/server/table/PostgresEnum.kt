package dev.cryptospace.tasket.server.table

import org.jetbrains.exposed.sql.Column

inline fun <reified T : Enum<T>> BaseTable.postgresEnum(name: String, typeName: String): Column<T> {
    return customEnumeration(
        name,
        typeName,
        { value -> enumValueOf<T>(value as String) },
        { it.name },
    )
}
