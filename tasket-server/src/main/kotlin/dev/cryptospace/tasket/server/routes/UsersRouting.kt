package dev.cryptospace.tasket.server.routes

import dev.cryptospace.tasket.server.repository.UserRepository
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.users() {
    get {
        handleGetAllRoute(UserRepository)
    }
}
