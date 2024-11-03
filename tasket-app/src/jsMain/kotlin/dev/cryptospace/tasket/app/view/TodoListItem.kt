package dev.cryptospace.tasket.app.view

import io.kvision.core.Container
import io.kvision.core.FlexDirection
import io.kvision.core.Widget
import io.kvision.form.check.checkBox
import io.kvision.html.ButtonStyle
import io.kvision.html.button
import io.kvision.html.div
import io.kvision.html.span
import io.kvision.panel.flexPanel

fun Container.todoListItem(
    id: String,
    label: String,
): Widget {
    return flexPanel(className = "list-group-item justify-content-between") {
        this.id = id
        flexPanel(FlexDirection.ROW) {
            checkBox()
            span(className = "list-group-item-title", content = label)
        }
        div {
            button(text = "", icon = "fas fa-trash", ButtonStyle.OUTLINEDANGER)
        }
    }
}
