package dev.cryptospace.tasket.server.security.login

import io.ktor.http.HttpStatusCode

object ExceededAttemptsLoginResult : LoginResult {
    override val statusCode = HttpStatusCode.TooManyRequests
    override val message = null
}
