package dev.cryptospace.tasket.server.security.login

import dev.cryptospace.tasket.payloads.authentication.LoginResponsePayload
import io.ktor.http.HttpStatusCode

class SuccessLoginResult(response: LoginResponsePayload) : LoginResult {
    override val statusCode = HttpStatusCode.OK
    override val message = response
}
