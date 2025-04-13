package dev.cryptospace.tasket.payloads.authentication

import dev.cryptospace.tasket.payloads.user.response.UserResponsePayload
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponsePayload(
    val accessToken: String,
    val refreshToken: String,
    val user: UserResponsePayload
)
