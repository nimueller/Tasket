package dev.cryptospace.tasket.payloads

import kotlinx.serialization.Serializable

@Serializable
data class TodoPayload(
    override var createdAt: Long = 0,
    override var updatedAt: Long = 0,
    var id: String? = null,
    var label: String
) : Payload {
    init {
        require(label.isNotBlank()) { "label cannot be blank" }
    }
}
