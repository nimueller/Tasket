package dev.cryptospace.tasket.app.model

import dev.cryptospace.tasket.app.network.HttpClient
import dev.cryptospace.tasket.app.network.get
import dev.cryptospace.tasket.payloads.todo.response.TodoStatusResponsePayload
import io.kvision.core.KVScope
import kotlinx.coroutines.launch

object TodoStatusModel {

    var statuses = listOf<TodoStatusResponsePayload>()
        private set

    init {
        KVScope.launch {
            val result = HttpClient.get<List<TodoStatusResponsePayload>>("/rest/statuses").handleStatusCodes()

            if (result != null) {
                statuses = result
            }
        }
    }
}
