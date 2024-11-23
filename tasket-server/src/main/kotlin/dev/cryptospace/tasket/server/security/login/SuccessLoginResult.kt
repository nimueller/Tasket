package dev.cryptospace.tasket.server.security.login

import io.ktor.http.HttpStatusCode

class SuccessLoginResult(token: String) : LoginResult {
    override val statusCode = HttpStatusCode.OK
    override val message = token
}
