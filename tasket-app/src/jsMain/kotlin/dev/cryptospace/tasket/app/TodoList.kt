package dev.cryptospace.tasket.app

import dev.cryptospace.tasket.app.model.TodoViewModel
import io.kvision.core.Container
import io.kvision.html.ButtonStyle
import io.kvision.html.button
import io.kvision.html.div
import io.kvision.html.li
import io.kvision.html.span
import io.kvision.html.ul
import io.kvision.panel.flexPanel
import io.kvision.state.bind

fun Container.todoList() {
    ul(className = "list-group placeholder-glow") {
        bind(TodoViewModel.todos, removeChildren = false) { payloads ->
            getChildren().forEach { component ->
                if (component.getAttribute("id") !in payloads.map { payload -> payload.id }) {
                    remove(component)
                }
            }

            payloads.forEach { payload ->
                if (payload.id !in getChildren().map { it.getAttribute("id") }) {
                    add(flexPanel(className = "list-group-item justify-content-between") {
                        id = payload.id
                        span(className = "list-group-item-title", content = payload.label)
                        div {
                            button(text = "", icon = "fas fa-trash", ButtonStyle.OUTLINEDANGER)
                        }
                    })
                }
            }
        }
        (1..20).forEach {
            li(className = "list-group-item placeholder col-4", content = "Test")
        }
        TodoViewModel.loadTodos()
    }
}
