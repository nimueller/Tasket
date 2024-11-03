package dev.cryptospace.tasket.server.table

import dev.cryptospace.tasket.payloads.TodoPayload
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder

object TodosTable : BaseTable<TodoPayload>() {
    val label = text("label")

    override fun ResultRow.toPayload(): TodoPayload {
        val payload = TodoPayload(id = this[id].toString(), label = this[label])
        writeBaseColumnsToPayload(payload)
        return payload
    }

    override fun UpdateBuilder<Int>.fromPayload(payload: TodoPayload) {
        this[label] = payload.label
    }
}
