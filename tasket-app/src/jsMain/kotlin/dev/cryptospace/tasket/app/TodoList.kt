package dev.cryptospace.tasket.app

import dev.cryptospace.tasket.app.model.TodoViewModel
import dev.cryptospace.tasket.payloads.TodoPayload
import external.Sortable
import io.kvision.core.Container
import io.kvision.core.FlexDirection
import io.kvision.form.check.checkBox
import io.kvision.html.ButtonStyle
import io.kvision.html.button
import io.kvision.html.div
import io.kvision.html.span
import io.kvision.panel.flexPanel
import io.kvision.state.bind
import org.w3c.dom.HTMLElement
import kotlin.js.json

fun Container.todoList() {
    div(className = "list-group") {
        bind(TodoViewModel.todos, removeChildren = false) { payloads ->
            getChildren().forEach { component ->
                if (component.getAttribute("id") !in payloads.map { payload -> payload.id }) {
                    remove(component)
                }
            }

            payloads.forEach { payload ->
                if (payload.id !in getChildren().map { it.getAttribute("id") }) {
                    todoListItem(payload)
                }
            }
        }
        TodoViewModel.loadTodos()

        addAfterInsertHook { node ->
            Sortable.create(
                node.elm!!.unsafeCast<HTMLElement>(),
                json(
                    "animation" to 200,
                ),
            )
        }
    }
}

private fun Container.todoListItem(payload: TodoPayload) {
    add(
        flexPanel(className = "list-group-item justify-content-between") {
            id = payload.id
            flexPanel(FlexDirection.ROW) {
                checkBox()
                span(className = "list-group-item-title", content = payload.label)
            }
            div {
                button(text = "", icon = "fas fa-trash", ButtonStyle.OUTLINEDANGER)
            }
        },
    )
}
