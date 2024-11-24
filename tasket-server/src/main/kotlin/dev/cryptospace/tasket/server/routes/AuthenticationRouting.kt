package dev.cryptospace.tasket.server.routes

import dev.cryptospace.tasket.payloads.authentication.LoginRequestPayload
import dev.cryptospace.tasket.payloads.authentication.RefreshTokenRequestPayload
import dev.cryptospace.tasket.server.security.login.SecureLogin
import dev.cryptospace.tasket.server.security.refresh.SecureRefresh
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.login() {
    post("/login") {
        val loginRequest = call.receive<LoginRequestPayload>()
        val loginResult = SecureLogin.tryLogin(loginRequest)
        val message = loginResult.message

        if (message == null) {
            call.respond(loginResult.statusCode)
        } else {
            call.respond(loginResult.statusCode, message)
        }
    }

    post("/refresh") {
        val refreshTokenRequest = call.receive<RefreshTokenRequestPayload>()
        val refreshResult = SecureRefresh.tryRefresh(refreshTokenRequest)
        val message = refreshResult.message

        if (message == null) {
            call.respond(refreshResult.statusCode)
        } else {
            call.respond(refreshResult.statusCode, message)
        }
    }
}
