package dev.cryptospace.tasket.server.utils

import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.routing.RoutingCall
import java.util.UUID

fun RoutingCall.userId(): UUID {
    val principal = principal<UserIdPrincipal>()?.name
    checkNotNull(principal) { "No principal found for user id" }
    return UUID.fromString(principal)
}
