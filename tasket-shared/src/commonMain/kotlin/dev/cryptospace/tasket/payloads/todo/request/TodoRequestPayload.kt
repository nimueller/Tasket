package dev.cryptospace.tasket.payloads.todo.request

import dev.cryptospace.tasket.payloads.RequestPayload
import kotlinx.serialization.Serializable

@Serializable
data class TodoRequestPayload(
    val label: String,
    val statusId: String? = null
) : RequestPayload
