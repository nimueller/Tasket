package dev.cryptospace.tasket.server.table

import dev.cryptospace.tasket.payloads.TodoCommentPayload

object TodoCommentsTable :
    BaseTable<TodoCommentPayload>("tasket.todo_comments", payloadCreator = { TodoCommentPayload() }) {
    private val comment = text("comment")

    init {
        registerStringPayload(comment, TodoCommentPayload::comment)
    }
}
