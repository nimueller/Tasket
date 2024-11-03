package dev.cryptospace.tasket.server.routes

import dev.cryptospace.tasket.payloads.TodoPayload
import dev.cryptospace.tasket.server.table.TodosTable
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.todo() {
    route("todo") {
        get {
            handleGetAllRoute<TodosTable, TodoPayload>()
        }
        get("{id}") {
            handleGetByIdRoute<TodosTable, TodoPayload>()
        }
        post {
            handlePostRoute<TodosTable, TodoPayload>()
        }
        put("{id}") {
            handleUpsertRoute<TodosTable, TodoPayload>()
        }
        delete("{id}") {
            handleDeleteRoute<TodosTable, TodoPayload>()
        }
    }
}
