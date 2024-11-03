package dev.cryptospace.tasket.payloads

import kotlinx.serialization.Serializable

@Serializable
data class TodoPayload(
    override var createdAt: String? = null,
    override var updatedAt: String? = null,
    var id: String? = null,
    var label: String,
) : Payload {
    init {
        require(label.isNotBlank()) { "label cannot be blank" }
    }
}
