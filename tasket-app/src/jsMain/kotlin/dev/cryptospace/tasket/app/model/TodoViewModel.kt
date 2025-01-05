package dev.cryptospace.tasket.app.model

import dev.cryptospace.tasket.app.network.HttpClient
import dev.cryptospace.tasket.app.network.get
import dev.cryptospace.tasket.payloads.todo.request.TodoRequestPayload
import io.kvision.core.KVScope
import io.kvision.state.ObservableListWrapper
import kotlinx.coroutines.launch

object TodoViewModel {
    val todos = ObservableListWrapper<TodoRequestPayload>()

    fun loadTodos() = KVScope.launch {
        todos.clear()
        todos.addAll(fetchTodos())
    }

    private suspend fun fetchTodos(): List<TodoRequestPayload> {
        return HttpClient.get<List<TodoRequestPayload>>("/rest/todos").handleStatusCodes() ?: emptyList()
    }
}
