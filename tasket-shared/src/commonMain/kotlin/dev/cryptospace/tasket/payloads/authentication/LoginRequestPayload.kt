package dev.cryptospace.tasket.payloads.authentication

import dev.cryptospace.tasket.payloads.Payload
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestPayload(val username: String, val password: String) : Payload()
