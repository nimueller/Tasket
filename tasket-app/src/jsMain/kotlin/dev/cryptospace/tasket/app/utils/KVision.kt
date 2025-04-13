package dev.cryptospace.tasket.app.utils

import dev.cryptospace.tasket.types.BootstrapColor
import io.kvision.core.BsColor
import io.kvision.core.Widget
import io.kvision.core.onEventLaunch
import io.kvision.html.Button
import org.w3c.dom.events.Event
import org.w3c.dom.events.FocusEvent
import org.w3c.dom.events.KeyboardEvent

inline fun <reified T : Widget> T.onEnterKeyUp(noinline handler: suspend T.(Event) -> Unit) {
    onEventLaunch("keyup") { event ->
        if (event is KeyboardEvent && event.key == "Enter") {
            handler(event)
        }
    }
}

fun Button.disable() {
    this.disabled = true
    this.addCssClass("disabled")
}

fun Button.enable() {
    this.disabled = false
    this.removeCssClass("disabled")
}


inline fun <reified T : Widget> T.onFocus(noinline handler: T.(FocusEvent) -> Unit): Int {
    return this.setEventListener<T> {
        focus = { event ->
            handler(event)
        }
    }
}

fun BootstrapColor.toBackgroundBsColor(): BsColor {
    return when (this) {
        BootstrapColor.PRIMARY -> BsColor.PRIMARYBG
        BootstrapColor.SECONDARY -> BsColor.SECONDARYBG
        BootstrapColor.SUCCESS -> BsColor.SUCCESSBG
        BootstrapColor.INFO -> BsColor.INFOBG
        BootstrapColor.WARNING -> BsColor.WARNINGBG
        BootstrapColor.DANGER -> BsColor.DANGERBG
    }
}
