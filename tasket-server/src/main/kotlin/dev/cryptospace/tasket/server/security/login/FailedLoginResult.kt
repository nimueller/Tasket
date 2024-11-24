package dev.cryptospace.tasket.server.security.login

import io.ktor.http.HttpStatusCode

object FailedLoginResult : LoginResult {
    override val statusCode = HttpStatusCode.Unauthorized
    override val message = null
}
