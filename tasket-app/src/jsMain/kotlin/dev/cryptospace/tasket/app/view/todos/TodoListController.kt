package dev.cryptospace.tasket.app.view.todos

import dev.cryptospace.tasket.app.network.HttpClient
import dev.cryptospace.tasket.app.network.delete
import dev.cryptospace.tasket.app.network.get
import dev.cryptospace.tasket.app.network.patch
import dev.cryptospace.tasket.app.network.post
import dev.cryptospace.tasket.app.view.dashboard.TodoDetailsModal
import dev.cryptospace.tasket.payloads.OptionalField
import dev.cryptospace.tasket.payloads.todo.request.TodoPatchRequestPayload
import dev.cryptospace.tasket.payloads.todo.request.TodoRequestPayload
import dev.cryptospace.tasket.payloads.todo.response.TodoResponsePayload
import io.kvision.core.KVScope
import io.kvision.core.onClickLaunch
import io.kvision.core.onEventLaunch
import kotlinx.coroutines.launch
import org.w3c.dom.events.KeyboardEvent

class TodoListController {

    val view = TodoListView()
    val todoDetailsModal = TodoDetailsModal(todoListController = this)

    init {
        view.todoInserter.onEventLaunch("keyup") { event ->
            if (event is KeyboardEvent && event.key == "Enter" && !value.isNullOrBlank()) {
                addTodo(value!!)
            }
        }

        view.onTodoInserted += { item, todo ->
            item.completeCheckbox.onClickLaunch {
                refreshTodos()
            }

            item.clickHandle.onEventLaunch("click") {
                todoDetailsModal.show()
                todoDetailsModal.refreshModal(todo.metaInformation.id)
            }

            item.deleteButton.onClickLaunch {
                HttpClient.delete("/rest/todos/${todo.metaInformation.id}").handleStatusCodes()
                refreshTodos()
            }
        }

        view.onTodoMoved += {
            KVScope.launch {
                for (entry in view.todoRenderer.entries) {
                    var todo = entry.item

                    println(todo.label + " " + entry.index)

                    HttpClient.patch<TodoPatchRequestPayload, TodoResponsePayload>(
                        "/rest/todos/${todo.metaInformation.id}",
                        TodoPatchRequestPayload(sortOrder = OptionalField.Present(entry.index)),
                    ).handleStatusCodes()
                }
            }
        }

        KVScope.launch { refreshTodos() }
    }

    suspend fun refreshTodos() {
        val todos = HttpClient.get<List<TodoResponsePayload>>("/rest/todos").handleStatusCodes()

        if (todos != null) {
            val sortedTodos = todos.sortedWith(
                compareBy<TodoResponsePayload> { it.sortOrder }.thenBy { it.metaInformation.createdAt },
            )

            view.todoRenderer.setItems(sortedTodos)
        }
    }

    suspend fun addTodo(label: String) {
        val payload = TodoRequestPayload(label = label, statusId = null)
        HttpClient.post("/rest/todos", payload).handleStatusCodes()
        view.todoInserter.value = null
        refreshTodos()
    }
}
