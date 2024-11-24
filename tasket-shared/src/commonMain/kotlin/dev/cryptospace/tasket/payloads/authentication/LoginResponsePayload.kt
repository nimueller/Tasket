package dev.cryptospace.tasket.payloads.authentication

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponsePayload(val accessToken: String, val refreshToken: String)
