package dev.cryptospace.tasket.server.table

import dev.cryptospace.tasket.payloads.TodoPayload
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder

object Todos : UUIDTable(), PayloadMapper<TodoPayload> {
    val label = text("label")

    override fun ResultRow.toPayload(): TodoPayload {
        return TodoPayload(id = this[id].toString(), label = this[label])
    }

    override fun UpdateBuilder<Int>.fromPayload(payload: TodoPayload) {
        this[label] = payload.label
    }
}
