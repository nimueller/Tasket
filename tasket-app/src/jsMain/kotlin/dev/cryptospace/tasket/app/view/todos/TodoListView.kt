package dev.cryptospace.tasket.app.view.todos

import dev.cryptospace.tasket.app.utils.SmartListRenderer
import dev.cryptospace.tasket.payloads.todo.response.TodoResponsePayload
import external.Sortable
import io.kvision.core.FlexDirection
import io.kvision.form.check.checkBox
import io.kvision.form.text.textInput
import io.kvision.html.Button
import io.kvision.html.ButtonStyle
import io.kvision.html.Div
import io.kvision.html.div
import io.kvision.html.span
import io.kvision.i18n.tr
import io.kvision.panel.FlexPanel
import io.kvision.panel.flexPanel
import org.w3c.dom.HTMLElement
import kotlin.js.json

class TodoListView : Div() {

    val onTodoInserted = mutableListOf<(TodoListItemComponents, TodoResponsePayload) -> Unit>()

    val todoInserter = textInput {
        placeholder = tr("New TODO")
    }

    private val todoContainer = div(className = "list-group mt-3") {
    }

    val todoRenderer = SmartListRenderer<TodoResponsePayload>(todoContainer) { todo ->
        val clickHandle = Div(className = "flex-grow-1 py-3") {
            span(className = "list-group-item-text", content = todo.label)
        }
        val deleteButton = Button(text = "", icon = "fas fa-trash", ButtonStyle.OUTLINEDANGER)

        val panel = FlexPanel(className = "list-group-item justify-content-between py-0") {
            flexPanel(FlexDirection.ROW, className = "flex-grow-1") {
                checkBox {
                    addCssClass("my-3")
                }
                add(clickHandle)
            }
            div(className = "my-3") {
                add(deleteButton)
            }
        }

        onTodoInserted.forEach { handler ->
            handler(
                TodoListItemComponents(
                    clickHandle = clickHandle,
                    deleteButton = deleteButton
                ), todo
            )
        }

        return@SmartListRenderer panel
    }

    init {
        todoContainer.addAfterInsertHook { node ->
            Sortable.create(
                node.elm!!.unsafeCast<HTMLElement>(),
                json(
                    "animation" to 200,
                ),
            )
        }
    }
}
