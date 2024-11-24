package dev.cryptospace.tasket.server.security.login

import io.ktor.http.HttpStatusCode

interface LoginResult {
    val statusCode: HttpStatusCode

    val message: Any?
}
