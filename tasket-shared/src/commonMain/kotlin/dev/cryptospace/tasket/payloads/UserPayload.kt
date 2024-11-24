package dev.cryptospace.tasket.payloads

import kotlinx.serialization.Serializable

@Serializable
data class UserPayload(val username: String, val password: String?) : Payload()
