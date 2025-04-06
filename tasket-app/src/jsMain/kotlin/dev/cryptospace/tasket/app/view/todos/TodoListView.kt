package dev.cryptospace.tasket.app.view.todos

import dev.cryptospace.tasket.app.utils.SmartListRenderer
import dev.cryptospace.tasket.payloads.todo.response.TodoResponsePayload
import external.Sortable
import external.jsonObject
import io.kvision.form.text.textInput
import io.kvision.html.Div
import io.kvision.html.div
import io.kvision.i18n.tr
import org.w3c.dom.HTMLElement

private const val ANIMATION_DURATION_IN_MILLIS = 200

class TodoListView : Div() {

    val onTodoInserted = mutableListOf<(TodoListItem, TodoResponsePayload) -> Unit>()

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
    }

    init {
        todoContainer.addAfterInsertHook { node ->
            Sortable.create(
                element = node.elm!!.unsafeCast<HTMLElement>(),
                options = jsonObject {
                    animation = ANIMATION_DURATION_IN_MILLIS
                }
            )
        }
    }
}
