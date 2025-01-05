package dev.cryptospace.tasket.server.user

import dev.cryptospace.tasket.server.routes.handleGetAllRoute
import dev.cryptospace.tasket.server.routes.handleGetByIdRoute
import dev.cryptospace.tasket.server.routes.handlePostRoute
import dev.cryptospace.tasket.server.routes.handlePutRoute
import dev.cryptospace.tasket.server.user.database.UserRepository
import dev.cryptospace.tasket.server.user.mapper.UserRequestMapper
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.util.getOrFail
import java.util.UUID

fun Route.users() {
    route("users") {
        get {
            handleGetAllRoute(UserRepository)
        }
        post {
            handlePostRoute(UserRepository, UserRequestMapper)
        }
        route("{id}") {
            get {
                handleGetByIdRoute(UserRepository, call.parameters.getOrFail<UUID>("id"))
            }
            put {
                handlePutRoute(UserRepository, UserRequestMapper, call.parameters.getOrFail<UUID>("id"))
            }
            patch("change-password") {
                val id = call.parameters.getOrFail("id")

                if (id == call.principal<UserIdPrincipal>()!!.name) {
                    call.respond(UserRepository.changePassword(UUID.fromString(id), call.receive()))
                } else {
                    call.respond(HttpStatusCode.Forbidden)
                }
            }
        }
    }
}
