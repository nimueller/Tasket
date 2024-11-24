package dev.cryptospace.tasket.server.security.refresh

import io.ktor.http.HttpStatusCode

object FailedRefreshResult : RefreshResult {
    override val statusCode = HttpStatusCode.Unauthorized
    override val message = null
}
