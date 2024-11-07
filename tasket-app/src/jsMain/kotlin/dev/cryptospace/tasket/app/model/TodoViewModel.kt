package dev.cryptospace.tasket.app.model

import dev.cryptospace.tasket.app.network.HttpClient
import dev.cryptospace.tasket.payloads.TodoPayload
import io.kvision.core.KVScope
import io.kvision.state.ObservableListWrapper
import kotlinx.coroutines.launch

object TodoViewModel {
    val todos = ObservableListWrapper<TodoPayload>()

    fun loadTodos() =
        KVScope.launch {
            todos.clear()
            todos.addAll(fetchTodos())
        }

    private suspend fun fetchTodos(): List<TodoPayload> {
        return HttpClient.get("todos") ?: emptyList()
    }
}
