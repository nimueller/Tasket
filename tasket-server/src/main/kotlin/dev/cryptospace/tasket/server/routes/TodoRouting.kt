package dev.cryptospace.tasket.server.routes

import dev.cryptospace.tasket.server.repository.TodoCommentRepository
import dev.cryptospace.tasket.server.repository.TodoRepository
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.util.getOrFail
import java.util.UUID

fun Route.todo() {
    route("todos") {
        get {
            handleGetAllRoute(TodoRepository)
        }
        post {
            handlePostRoute(TodoRepository)
        }
        route("{todoId}") {
            get {
                handleGetByIdRoute(TodoRepository, call.parameters.getOrFail<UUID>("todoId"))
            }
            put {
                handlePatchRoute(TodoRepository, call.parameters.getOrFail<UUID>("todoId"))
            }
            delete {
                handleDeleteRoute(TodoRepository, call.parameters.getOrFail<UUID>("todoId"))
            }
        }
        route("{todoId}") {
            comments()
        }
    }
}

private fun Route.comments() {
    route("comments") {
        get {
            val comments = TodoCommentRepository.getAllCommentsForTodo(call.parameters.getOrFail<UUID>("todoId"))
            call.respond(comments)
        }
        post {
            handlePostRoute(TodoCommentRepository)
        }
    }
}
