package dev.cryptospace.tasket.app.model

import dev.cryptospace.tasket.payloads.authentication.LoginRequestPayload
import kotlinx.serialization.Serializable

@Serializable
data class LoginDataModel(
    var username: String? = null,
    var password: String? = null,
    var rememberMe: Boolean? = false,
    var loginSuccess: Boolean = false,
) {
    fun toPayload() = LoginRequestPayload(username ?: "", password ?: "")
}
