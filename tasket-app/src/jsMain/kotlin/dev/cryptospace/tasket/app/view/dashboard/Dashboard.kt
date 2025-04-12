package dev.cryptospace.tasket.app.view.dashboard

import io.kvision.core.Container
import io.kvision.core.FontStyle
import io.kvision.core.FontWeight
import io.kvision.core.Style
import io.kvision.html.Div

val headerStyle = Style {
    fontFamily = "Boldonse, system-ui"
    fontWeight = FontWeight.BOLD
    fontStyle = FontStyle.NORMAL
}

class Dashboard : Div(className = "p-3") {

}

fun Container.dashboard() {
}
