package dev.cryptospace.tasket.app.view.dashboard

import dev.cryptospace.tasket.app.model.TodoViewModel
import dev.cryptospace.tasket.payloads.todo.response.TodoResponsePayload
import external.Sortable
import io.kvision.core.Container
import io.kvision.core.FlexDirection
import io.kvision.core.onClickLaunch
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
                if (component.getAttribute("id") !in payloads.map { payload -> payload.metaInformation.id }) {
                    remove(component)
                }
            }

            payloads.forEach { payload ->
                if (payload.metaInformation.id !in getChildren().map { it.getAttribute("id") }) {
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

private fun Container.todoListItem(payload: TodoResponsePayload) {
    add(
        flexPanel(className = "list-group-item justify-content-between") {
            onClickLaunch {
                TodoDetails.modal.show()
                TodoDetails.refreshModal(payload.metaInformation.id)
            }
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
