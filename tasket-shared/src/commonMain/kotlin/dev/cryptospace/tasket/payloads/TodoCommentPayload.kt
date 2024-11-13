package dev.cryptospace.tasket.payloads

import kotlinx.serialization.Serializable

@Serializable
class TodoCommentPayload : Payload() {
    var comment: String = ""
}
