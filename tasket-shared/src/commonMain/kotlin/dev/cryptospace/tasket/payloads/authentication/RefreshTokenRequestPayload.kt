package dev.cryptospace.tasket.payloads.authentication

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequestPayload(val refreshToken: String)
