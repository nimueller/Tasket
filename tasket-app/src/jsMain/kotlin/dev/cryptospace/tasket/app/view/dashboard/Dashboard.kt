package dev.cryptospace.tasket.app.view.dashboard

import dev.cryptospace.tasket.app.model.TodoStatusModel
import dev.cryptospace.tasket.app.view.todos.TodoListController
import io.kvision.core.Container
import io.kvision.core.FontStyle
import io.kvision.core.FontWeight
import io.kvision.core.Style
import io.kvision.html.ButtonStyle
import io.kvision.html.div
import io.kvision.html.h3
import io.kvision.theme.themeSwitcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

val headerStyle = Style {
    fontFamily = "Boldonse, system-ui"
    fontWeight = FontWeight.BOLD
    fontStyle = FontStyle.NORMAL
}

fun Container.dashboard() {
    GlobalScope.launch {
        TodoStatusModel.init()
        div(className = "p-3") {
            div(className = "clearfix my-3") {
                h3(content = "TASKET", className = "float-start") {
                    addCssStyle(headerStyle)
                }
                themeSwitcher(style = ButtonStyle.OUTLINESECONDARY, className = "float-end")
            }
            val controller = TodoListController()
            add(controller.view)
        }
    }
}
