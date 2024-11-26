package dev.cryptospace.tasket.app.utils

import io.kvision.core.Widget
import io.kvision.core.onEventLaunch
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent

inline fun <reified T : Widget> T.onEnterKeyUp(noinline handler: suspend T.(Event) -> Unit) {
    onEventLaunch("keyup") { event ->
        if (event is KeyboardEvent && event.key == "Enter") {
            handler(event)
        }
    }
}
