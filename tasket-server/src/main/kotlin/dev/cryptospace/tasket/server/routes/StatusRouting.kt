package dev.cryptospace.tasket.server.routes

import dev.cryptospace.tasket.server.repository.TodoStatusRepository
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.util.getOrFail
import java.util.UUID

fun Route.status() {
    route("statuses") {
        get {
            handleGetAllRoute(TodoStatusRepository)
        }
        get("{todoId}") {
            handleGetByIdRoute(TodoStatusRepository, call.parameters.getOrFail<UUID>("todoId"))
        }
    }
}
