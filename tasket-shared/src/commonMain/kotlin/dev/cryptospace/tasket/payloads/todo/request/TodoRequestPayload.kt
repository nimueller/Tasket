package dev.cryptospace.tasket.payloads.todo.request

import dev.cryptospace.tasket.payloads.RequestPayload
import kotlinx.serialization.Serializable

@Serializable
data class TodoRequestPayload(val label: String, val statusId: String?) : RequestPayload {
    init {
        require(label.isNotBlank()) { "Label must not be blank" }
    }
}
