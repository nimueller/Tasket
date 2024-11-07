package dev.cryptospace.tasket.server.table

import dev.cryptospace.tasket.payloads.TodoCommentPayload
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder

object TodoCommentsTable : BaseTable<TodoCommentPayload>("todo_comments") {
    val todo = uuid("todo_id").references(id)
    val comment = text("comment")

    override fun ResultRow.toPayload(): TodoCommentPayload {
        val payload = TodoCommentPayload(id = this[id].toString(), comment = this[comment])
        writeBaseColumnsToPayload(payload)
        return payload
    }

    override fun UpdateBuilder<Int>.fromPayload(payload: TodoCommentPayload) {
        this[comment] = payload.comment
    }
}
