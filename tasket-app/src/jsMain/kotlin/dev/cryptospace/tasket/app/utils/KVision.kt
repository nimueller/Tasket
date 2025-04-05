package dev.cryptospace.tasket.app.utils

import dev.cryptospace.tasket.types.BootstrapColor
import io.kvision.core.BsColor
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
