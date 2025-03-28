package dev.cryptospace.tasket.app.model

import dev.cryptospace.tasket.app.network.HttpClient
import dev.cryptospace.tasket.app.network.get
import dev.cryptospace.tasket.payloads.todo.response.TodoResponsePayload
import io.kvision.core.KVScope
import io.kvision.state.ObservableListWrapper
import kotlinx.coroutines.launch

object TodoViewModel {
    val todos = ObservableListWrapper<TodoResponsePayload>()

    fun loadTodos() = KVScope.launch {
        todos.clear()
        todos.addAll(fetchTodos())
    }

    private suspend fun fetchTodos(): List<TodoResponsePayload> {
        return HttpClient.get<List<TodoResponsePayload>>("/rest/todos").handleStatusCodes() ?: emptyList()
    }
}
