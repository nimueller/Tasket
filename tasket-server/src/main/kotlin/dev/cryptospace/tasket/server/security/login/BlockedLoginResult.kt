package dev.cryptospace.tasket.server.security.login

import io.ktor.http.HttpStatusCode

object BlockedLoginResult : LoginResult {
    // TODO implement behavior for blocked users
    override val statusCode = HttpStatusCode.Forbidden
    override val message = null
}
