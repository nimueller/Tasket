package dev.cryptospace.tasket.payloads

import kotlinx.serialization.Serializable

@Serializable
data class TodoStatusChangePayload(val oldStatus: String, val newStatus: String) : Payload()
