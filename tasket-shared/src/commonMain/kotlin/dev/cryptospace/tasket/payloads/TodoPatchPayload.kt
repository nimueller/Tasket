package dev.cryptospace.tasket.payloads

import kotlinx.serialization.Serializable

@Serializable
data class TodoPatchPayload(val label: String?) : Payload()
