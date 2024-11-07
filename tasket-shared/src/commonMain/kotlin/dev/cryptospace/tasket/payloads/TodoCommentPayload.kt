package dev.cryptospace.tasket.payloads

import kotlinx.serialization.Serializable

@Serializable
data class TodoCommentPayload(
    override var createdAt: String? = null,
    override var updatedAt: String? = null,
    var id: String? = null,
    var comment: String,
) : Payload
