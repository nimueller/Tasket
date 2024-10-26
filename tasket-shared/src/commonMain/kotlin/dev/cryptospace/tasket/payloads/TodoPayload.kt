package dev.cryptospace.tasket.payloads

import kotlinx.serialization.Serializable

@Serializable
data class TodoPayload(val id: String? = null, val label: String) : Payload {
    init {
        require(label.isNotBlank()) { "label cannot be blank" }
    }
}
