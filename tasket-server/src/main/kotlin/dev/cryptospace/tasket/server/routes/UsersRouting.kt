package dev.cryptospace.tasket.server.routes

import dev.cryptospace.tasket.server.repository.UserRepository
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.util.getOrFail
import java.util.UUID

fun Route.users() {
    authenticate("admin") {
        route("users") {
            get {
                handleGetAllRoute(UserRepository)
            }
            post {
                handlePostRoute(UserRepository)
            }
            route("{id}") {
                get {
                    handleGetByIdRoute(UserRepository, call.parameters.getOrFail<UUID>("id"))
                }
                put {
                    handlePutRoute(UserRepository, call.parameters.getOrFail<UUID>("id"))
                }
            }
        }
    }
}
