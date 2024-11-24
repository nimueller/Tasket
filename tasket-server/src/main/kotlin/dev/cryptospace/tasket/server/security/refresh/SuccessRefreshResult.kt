package dev.cryptospace.tasket.server.security.refresh

import dev.cryptospace.tasket.payloads.authentication.LoginResponsePayload
import io.ktor.http.HttpStatusCode

class SuccessRefreshResult(loginResponse: LoginResponsePayload) : RefreshResult {
    override val statusCode = HttpStatusCode.OK
    override val message = loginResponse
}
