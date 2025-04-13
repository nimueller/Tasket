package dev.cryptospace.tasket.app.model

import dev.cryptospace.tasket.app.network.HttpClient
import dev.cryptospace.tasket.app.network.get
import dev.cryptospace.tasket.payloads.user.response.UserResponsePayload

object UserSession {
    private var user: UserResponsePayload? = null

    suspend fun getMe(): UserResponsePayload {
        val currentUser = user

        if (currentUser != null) {
            return currentUser
        }

        return refreshUser().also { user = it }
    }

    private suspend fun refreshUser(): UserResponsePayload {
        val response = HttpClient.get<UserResponsePayload>("/rest/users/me").handleStatusCodes()
        checkNotNull(response)
        return response
    }
}
