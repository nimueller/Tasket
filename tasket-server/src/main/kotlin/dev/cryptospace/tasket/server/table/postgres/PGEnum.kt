package dev.cryptospace.tasket.server.table.postgres

import org.postgresql.util.PGobject

class PGEnum<T : Enum<T>>(typeName: String, enumValue: T?) : PGobject() {
    init {
        this.type = typeName
        this.value = enumValue?.name
    }
}
