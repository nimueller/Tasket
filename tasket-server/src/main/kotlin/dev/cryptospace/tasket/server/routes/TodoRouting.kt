package dev.cryptospace.tasket.server.routes

import dev.cryptospace.tasket.server.repository.TodoCommentsRepository
import dev.cryptospace.tasket.server.repository.TodosRepository
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
            handleGetAllRoute(TodosRepository)
        }
        post {
            handlePostRoute(TodosRepository)
        }
        route("{todoId}") {
            get {
                handleGetByIdRoute(TodosRepository, call.parameters.getOrFail<UUID>("todoId"))
            }
            put {
                handlePatchRoute(TodosRepository, call.parameters.getOrFail<UUID>("todoId"))
            }
            delete {
                handleDeleteRoute(TodosRepository, call.parameters.getOrFail<UUID>("todoId"))
            }
            comments()
        }
    }
}

private fun Route.comments() {
    route("comments") {
        get {
            val comments = TodoCommentsRepository.getAllCommentsForTodo(call.parameters.getOrFail<UUID>("todoId"))
            call.respond(comments)
        }
        post {
            handlePostRoute(TodoCommentsRepository)
        }
    }
}
