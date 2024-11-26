package dev.cryptospace.tasket.app.components

import io.kvision.core.AlignItems
import io.kvision.core.Container
import io.kvision.core.JustifyContent
import io.kvision.html.div
import io.kvision.html.icon
import io.kvision.panel.flexPanel

fun Container.iconFormGroup(iconClass: String, content: Container.() -> Unit) =
    div(className = "form-group row kv-mb-3") {
        flexPanel(
            justify = JustifyContent.CENTER,
            alignItems = AlignItems.CENTER,
            className = "col-sm-1 col-form-label",
        ) {
            icon(iconClass)
        }
        flexPanel(
            alignItems = AlignItems.CENTER,
            className = "col-sm-11",
        ) {
            content()
        }
    }
