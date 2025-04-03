package dev.cryptospace.tasket.app.view.todos

import dev.cryptospace.tasket.app.utils.SmartListRenderer
import dev.cryptospace.tasket.payloads.todo.response.TodoResponsePayload
import external.Sortable
import io.kvision.core.FlexDirection
import io.kvision.form.check.checkBox
import io.kvision.form.text.textInput
import io.kvision.html.ButtonStyle
import io.kvision.html.Div
import io.kvision.html.button
import io.kvision.html.div
import io.kvision.html.span
import io.kvision.i18n.tr
import io.kvision.panel.FlexPanel
import io.kvision.panel.flexPanel
import org.w3c.dom.HTMLElement
import kotlin.js.json

class TodoListView : Div() {

    val onTodoInserted = mutableListOf<(FlexPanel, TodoResponsePayload) -> Unit>()

    val todoInserter = textInput {
        placeholder = tr("New TODO")
    }

    private val todoContainer = div(className = "list-group mt-3") {
    }

    val todoRenderer = SmartListRenderer<TodoResponsePayload>(todoContainer) { todo ->
        val panel = FlexPanel(className = "list-group-item justify-content-between") {
            flexPanel(FlexDirection.ROW) {
                checkBox()
                span(className = "list-group-item-title", content = todo.label)
            }
            div {
                button(text = "", icon = "fas fa-trash", ButtonStyle.OUTLINEDANGER)
            }
        }

        onTodoInserted.forEach { it(panel, todo) }

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
