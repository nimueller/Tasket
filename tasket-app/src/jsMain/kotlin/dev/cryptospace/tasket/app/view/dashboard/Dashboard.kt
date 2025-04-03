package dev.cryptospace.tasket.app.view.dashboard

import dev.cryptospace.tasket.app.view.todos.TodoListController
import io.kvision.core.Container
import io.kvision.html.ButtonStyle
import io.kvision.html.div
import io.kvision.theme.themeSwitcher

fun Container.dashboard() {
    div(className = "p-3") {
        themeSwitcher(style = ButtonStyle.OUTLINESECONDARY)
        val controller = TodoListController()
        add(controller.view)
    }
}
