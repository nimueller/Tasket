package dev.cryptospace.tasket.server.table

import dev.cryptospace.tasket.payloads.TodoPayload

object TodosTable : BaseTable<TodoPayload>(name = "tasket.todos", payloadCreator = { TodoPayload() }) {
    private val label = text("label")

    init {
        registerStringPayload(label, TodoPayload::label)
    }
}
