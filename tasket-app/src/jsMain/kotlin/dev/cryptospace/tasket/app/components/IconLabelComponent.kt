package dev.cryptospace.tasket.app.components

import io.kvision.core.AlignItems
import io.kvision.core.JustifyContent
import io.kvision.core.Widget
import io.kvision.html.Div
import io.kvision.html.Icon
import io.kvision.panel.SimplePanel
import io.kvision.panel.flexPanel

fun <W : Widget> SimplePanel.iconLabelComponent(
    icon: String? = null,
    body: W
): IconLabelComponent<W> {
    return IconLabelComponent(icon, body).also { add(it) }
}

class IconLabelComponent<W : Widget>(
    icon: String? = null,
    val body: W
) : Div(className = "form-group row kv-mb-3") {

    var icon: String? = icon
        set(value) {
            field = value
            refreshIcon()
        }

    val iconComponent = Icon(icon = icon ?: "fa fa-question-circle")

    init {
        flexPanel(
            justify = JustifyContent.CENTER,
            alignItems = AlignItems.CENTER,
            className = "col-sm-1 col-form-label",
        ) {
            add(iconComponent)
        }
        flexPanel(
            alignItems = AlignItems.CENTER,
            className = "col-sm-11",
        ) {
            add(body)
        }
    }

    private fun refreshIcon() {
        iconComponent.icon = icon ?: "fa fa-question-circle"
    }
}
