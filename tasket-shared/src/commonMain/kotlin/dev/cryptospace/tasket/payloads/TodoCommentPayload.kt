package dev.cryptospace.tasket.payloads

import kotlinx.serialization.Serializable

@Serializable
data class TodoCommentPayload(val comment: String) : Payload()
