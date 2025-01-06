package dev.cryptospace.tasket.payloads.authentication

import dev.cryptospace.tasket.payloads.RequestPayload
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestPayload(val username: String, val password: String) : RequestPayload {
    init {
        require(username.isNotBlank()) { "Username must not be blank" }
        require(password.isNotBlank()) { "Password must not be blank" }
    }
}
