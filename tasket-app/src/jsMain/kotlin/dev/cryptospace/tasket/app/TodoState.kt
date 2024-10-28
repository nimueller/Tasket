package dev.cryptospace.tasket.app

import dev.cryptospace.tasket.app.network.HttpClient
import dev.cryptospace.tasket.payloads.TodoPayload
import io.kvision.state.ObservableListWrapper

object TodoState {
    val todos = ObservableListWrapper<TodoPayload>()

    suspend fun refreshTodos() {
        todos.clear()
        todos.addAll(requireNotNull(HttpClient.get<List<TodoPayload>>("todo")))
    }
}
