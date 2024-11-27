package dev.cryptospace.tasket.app.view.dashboard

import io.kvision.core.Container
import io.kvision.core.FlexDirection
import io.kvision.core.Style
import io.kvision.html.ButtonStyle
import io.kvision.html.div
import io.kvision.panel.flexPanel
import io.kvision.theme.themeSwitcher

fun Container.dashboard() {
    div(className = "p-3") {
        flexPanel(direction = FlexDirection.ROW) {
            addCssStyle(
                Style {
                    setStyle("gap", "1rem")
                },
            )
            todoInserter()
            themeSwitcher(style = ButtonStyle.OUTLINESECONDARY)
        }
        div(className = "mt-3") {
            todoList()
        }
    }
}
