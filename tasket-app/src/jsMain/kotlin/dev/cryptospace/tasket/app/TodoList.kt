package dev.cryptospace.tasket.app

import io.kvision.core.Container
import io.kvision.core.FlexDirection
import io.kvision.html.div
import io.kvision.panel.flexPanel

fun Container.todoList() {
    flexPanel(FlexDirection.COLUMN, spacing = 5) {
        (1..200).forEach { i ->
            options {
                div(i.toString())
            }
        }
    }
}
