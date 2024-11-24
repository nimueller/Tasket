package dev.cryptospace.tasket.server.security.refresh

import io.ktor.http.HttpStatusCode

interface RefreshResult {
    val statusCode: HttpStatusCode

    val message: Any?
}
