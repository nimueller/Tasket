package dev.cryptospace.tasket.app

import io.kvision.core.Container
import io.kvision.core.FlexDirection
import io.kvision.core.KVScope
import io.kvision.html.span
import io.kvision.panel.flexPanel
import kotlinx.coroutines.launch

fun Container.todoList() {
    flexPanel(FlexDirection.COLUMN, spacing = 5) {
        TodoState.todos.subscribe { payloads ->
            removeAll()

            payloads.forEach { payload ->
                span(payload.label)
            }
        }
        KVScope.launch {
            TodoState.refreshTodos()
        }
    }
}
