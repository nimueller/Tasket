package dev.cryptospace.tasket.payloads.authentication

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestPayload(val username: String, val password: String)
