package dev.cryptospace.tasket.app.view.dashboard

import dev.cryptospace.tasket.app.view.todos.TodoListController
import io.kvision.core.Container
import io.kvision.core.FontStyle
import io.kvision.core.FontWeight
import io.kvision.core.Style
import io.kvision.html.ButtonStyle
import io.kvision.html.div
import io.kvision.html.h1
import io.kvision.theme.themeSwitcher

val headerStyle = Style {
    fontFamily = "Boldonse, system-ui"
    fontWeight = FontWeight.BOLD
    fontStyle = FontStyle.NORMAL
}

fun Container.dashboard() {
    div(className = "p-3") {
        div(className = "clearfix my-3") {
            h1(content = "TASKET", className = "float-start") {
                addCssStyle(headerStyle)
            }
            themeSwitcher(style = ButtonStyle.OUTLINESECONDARY, className = "float-end")
        }
        val controller = TodoListController()
        add(controller.view)
    }
}
