package dev.cryptospace.tasket.payloads.user.request

import dev.cryptospace.tasket.payloads.RequestPayload
import kotlinx.serialization.Serializable

@Serializable
data class UserChangePasswordRequestPayload(
    val currentPassword: String,
    val newPassword: String,
    val repeatNewPassword: String
) : RequestPayload
