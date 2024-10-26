package dev.cryptospace.tasket.server.routes

import dev.cryptospace.tasket.payloads.TodoPayload
import dev.cryptospace.tasket.server.table.Todos
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.todo() {
    route("todo") {
        get {
            handleGetAllRoute<Todos, TodoPayload>()
        }
        get("{id}") {
            handleGetByIdRoute<Todos, TodoPayload>()
        }
        post {
            handlePostRoute<Todos, TodoPayload>()
        }
        put("{id}") {
            handleUpsertRoute<Todos, TodoPayload>()
        }
        delete("{id}") {
            handleDeleteRoute<Todos, TodoPayload>()
        }
    }
}
