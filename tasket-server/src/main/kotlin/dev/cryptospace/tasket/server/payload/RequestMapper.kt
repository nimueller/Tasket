package dev.cryptospace.tasket.server.payload

import dev.cryptospace.tasket.payloads.RequestPayload
import dev.cryptospace.tasket.server.table.BaseTable
import io.ktor.server.auth.UserIdPrincipal
import org.jetbrains.exposed.sql.statements.UpdateBuilder

fun interface RequestMapper<T : BaseTable, P : RequestPayload> {
    fun mapFromPayload(principal: UserIdPrincipal, table: T, payload: P, updateBuilder: UpdateBuilder<Int>)
}
