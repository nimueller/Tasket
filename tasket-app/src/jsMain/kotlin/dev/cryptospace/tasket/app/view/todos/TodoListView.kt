package dev.cryptospace.tasket.app.view.todos

import dev.cryptospace.tasket.app.utils.SmartListRenderer
import dev.cryptospace.tasket.payloads.todo.response.TodoResponsePayload
import io.kvision.form.text.textInput
import io.kvision.html.Div
import io.kvision.html.div
import io.kvision.i18n.tr

class TodoListView : Div() {

    val onTodoInserted = mutableListOf<(TodoListItem, TodoResponsePayload) -> Unit>()
    val onTodoMoved = mutableListOf<() -> Unit>()

    val todoInserter = textInput {
        placeholder = tr("New TODO")
    }

    private val todoContainer = div(className = "list-group mt-3") {
    }

    val todoRenderer = SmartListRenderer<TodoResponsePayload>(todoContainer) { todo ->
        val item = TodoListItem(todo)

        onTodoInserted.forEach { handler ->
            handler(item, todo)
        }

        return@SmartListRenderer item
    }.makeSortable { _ ->
        onTodoMoved.forEach { callback ->
            callback.invoke()
        }
    }
}
