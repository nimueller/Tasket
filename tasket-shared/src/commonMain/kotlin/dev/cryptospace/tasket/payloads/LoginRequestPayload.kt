package dev.cryptospace.tasket.payloads

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestPayload(val username: String, val password: String)
