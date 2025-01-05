package dev.cryptospace.tasket.payloads.user.request

import dev.cryptospace.tasket.payloads.RequestPayload
import kotlinx.serialization.Serializable

@Serializable
data class UserRequestPayload(val username: String, val password: String) : RequestPayload
