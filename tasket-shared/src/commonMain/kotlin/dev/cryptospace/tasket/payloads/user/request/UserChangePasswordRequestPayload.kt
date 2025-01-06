package dev.cryptospace.tasket.payloads.user.request

import dev.cryptospace.tasket.payloads.RequestPayload
import kotlinx.serialization.Serializable

@Serializable
data class UserChangePasswordRequestPayload(val password: String) : RequestPayload {
    init {
        require(password.isNotBlank()) { "Password must not be blank" }
    }
}
