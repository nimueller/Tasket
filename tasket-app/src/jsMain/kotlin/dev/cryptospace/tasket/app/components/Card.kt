package dev.cryptospace.tasket.app.components

import io.kvision.core.Container
import io.kvision.html.Div
import io.kvision.html.div
import io.kvision.html.h2

fun Container.card(title: String? = null, className: String? = null, content: Div.() -> Unit = {}) {
    div(className = "card $className") {
        div(className = "card-body") {
            if (title != null) {
                h2(className = "card-title mb-4") {
                    +title
                }
            }
            content()
        }
    }
}
