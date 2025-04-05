package dev.cryptospace.tasket.app.model

import dev.cryptospace.tasket.app.network.HttpClient
import dev.cryptospace.tasket.app.network.get
import dev.cryptospace.tasket.payloads.todo.response.TodoResponsePayload
import dev.cryptospace.tasket.payloads.todo.response.TodoStatusResponsePayload

object TodoStatusModel {

    var statuses = listOf<TodoStatusResponsePayload>()
        private set

    suspend fun init() {
        val result = HttpClient.get<List<TodoStatusResponsePayload>>("/rest/statuses").handleStatusCodes()

        if (result != null) {
            statuses = result
        }
    }

    fun getStatus(todo: TodoResponsePayload): TodoStatusResponsePayload {
        return getStatusById(todo.statusId)
    }

    fun getStatusById(id: String): TodoStatusResponsePayload {
        val status = getStatusByIdOrNull(id)
        checkNotNull(status) { "Status not found for ID: $id" }
        return status
    }

    fun getStatusByIdOrNull(id: String): TodoStatusResponsePayload? {
        return statuses.find { it.metaInformation.id == id }
    }
}
