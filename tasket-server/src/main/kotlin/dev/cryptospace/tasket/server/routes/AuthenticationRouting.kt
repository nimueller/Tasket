package dev.cryptospace.tasket.server.routes

import dev.cryptospace.tasket.payloads.LoginRequestPayload
import dev.cryptospace.tasket.server.security.login.SecureLogin
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
}
