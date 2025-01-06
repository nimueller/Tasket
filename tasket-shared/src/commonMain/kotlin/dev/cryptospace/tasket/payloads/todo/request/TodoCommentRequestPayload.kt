package dev.cryptospace.tasket.payloads.todo.request

import dev.cryptospace.tasket.payloads.RequestPayload
import kotlinx.serialization.Serializable

@Serializable
data class TodoCommentRequestPayload(val comment: String) : RequestPayload {
    init {
        require(comment.isNotBlank()) { "Comment must not be blank" }
    }
}
