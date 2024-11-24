package dev.cryptospace.tasket.server.security.login

import io.ktor.http.HttpStatusCode

object BlockedLoginResult : LoginResult {
    override val statusCode = HttpStatusCode.Forbidden
    override val message = null
}
