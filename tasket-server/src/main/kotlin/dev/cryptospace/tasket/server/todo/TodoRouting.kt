package dev.cryptospace.tasket.server.todo

import dev.cryptospace.tasket.server.repository.TodoStatusChangeRepository
import dev.cryptospace.tasket.server.routes.handleDeleteRoute
import dev.cryptospace.tasket.server.routes.handleGetAllRoute
import dev.cryptospace.tasket.server.routes.handleGetByIdRoute
import dev.cryptospace.tasket.server.routes.handlePostRoute
import dev.cryptospace.tasket.server.routes.handlePutRoute
import dev.cryptospace.tasket.server.routes.validateExistingItemIsOwnedByUser
import dev.cryptospace.tasket.server.todo.database.TodoCommentRepository
import dev.cryptospace.tasket.server.todo.database.TodoCommentsTable
import dev.cryptospace.tasket.server.todo.database.TodoRepository
import dev.cryptospace.tasket.server.todo.mapper.TodoCommentRequestMapper
import dev.cryptospace.tasket.server.todo.mapper.TodoRequestMapper
import dev.cryptospace.tasket.server.utils.userId
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.util.getOrFail
import java.util.UUID

fun Route.todo() = route("todos") {
    get {
        handleGetAllRoute(TodoRepository)
    }
    post {
        handlePostRoute(TodoRepository, TodoRequestMapper)
    }
    route("{todoId}") {
        get {
            handleGetByIdRoute(TodoRepository, call.parameters.getOrFail<UUID>("todoId"))
        }
        put {
            handlePutRoute(TodoRepository, TodoRequestMapper, call.parameters.getOrFail<UUID>("todoId"))
        }
        delete {
            handleDeleteRoute(TodoRepository, call.parameters.getOrFail<UUID>("todoId"))
        }

        comments()
        statusChanges()
    }
}

private fun Route.comments() = route("comments") {
    get {
        val todoId = call.parameters.getOrFail<UUID>("todoId")
        validateExistingItemIsOwnedByUser(TodoRepository, todoId)
        call.respond(TodoCommentRepository.getAllCommentsForTodo(todoId, call.userId()))
    }
    post {
        val todoId = call.parameters.getOrFail<UUID>("todoId")
        validateExistingItemIsOwnedByUser(TodoRepository, todoId)
        handlePostRoute(TodoCommentRepository, TodoCommentRequestMapper) { this[TodoCommentsTable.todoId] = todoId }
    }
    put("{commentId}") {
        val todoId = call.parameters.getOrFail<UUID>("todoId")
        validateExistingItemIsOwnedByUser(TodoRepository, todoId)
        handlePutRoute(TodoCommentRepository, TodoCommentRequestMapper, todoId)
    }
}

private fun Route.statusChanges() = route("statusChanges") {
    get {
        val todoId = call.parameters.getOrFail<UUID>("todoId")
        validateExistingItemIsOwnedByUser(TodoRepository, todoId)
        call.respond(TodoStatusChangeRepository.getAllCommentsForTodo(todoId))
    }
}
