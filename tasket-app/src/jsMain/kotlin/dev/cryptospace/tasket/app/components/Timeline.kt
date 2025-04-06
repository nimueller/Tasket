package dev.cryptospace.tasket.app.components

import io.kvision.core.Background
import io.kvision.core.Color
import io.kvision.core.Container
import io.kvision.core.Display
import io.kvision.core.ListStyle
import io.kvision.core.ListStyleType
import io.kvision.core.PClass
import io.kvision.core.Position
import io.kvision.core.Style
import io.kvision.html.Li
import io.kvision.html.Ul
import io.kvision.html.div
import io.kvision.html.h5
import io.kvision.utils.perc
import io.kvision.utils.px
import io.kvision.utils.rem

val timelineStyle = Style(".timeline") {
    position = Position.RELATIVE
    listStyle = ListStyle(ListStyleType.NONE)
}

val timelineStyleAfter = Style(".timeline::after") {
    setStyle("content", "\"\"")
    position = Position.ABSOLUTE
    width = 2.px
    left = 0.px
    top = 0.5.rem
    bottom = 0.px
    background = Background(Color.rgb(255, 255, 255))
}

val timelineItemStyle = Style(".timeline-item") {
    position = Position.RELATIVE
    paddingBottom = 2.rem
}

val timelineItemStyleAfter = Style(".timeline-item::after") {
    setStyle("content", "\"\"")
    background = Background(color = Color.rgb(255, 255, 255))
    position = Position.ABSOLUTE
    display = Display.BLOCK
    top = 0.5.rem
    setStyle("left", "calc(-2rem - 5px)")
    borderRadius = 50.perc
    width = 12.px
    height = 12.px
}

val timelineItemLastChildStyle = Style(".timeline-item", pClass = PClass.LASTCHILD) {
    paddingBottom = 0.px
}

fun Container.timeline(init: Timeline.() -> Unit = {}): Timeline {
    val child = Timeline(init)
    this.add(child)
    return child
}

class Timeline(init: Timeline.() -> Unit = {}) : Ul() {

    init {
        addCssStyle(timelineStyle)
        addCssStyle(timelineStyleAfter)
        init()
    }

    fun clearItems() {
        this.removeAll()
    }

    fun item(timestamp: String, init: TimelineItem.() -> Unit) {
        this.add(TimelineItem(timestamp).apply(init))
    }
}

open class TimelineItem(timestamp: String) : Li() {

    val header = div(className = "clearfix") {
        div(className = "float-start") {
            h5(content = timestamp)
        }
    }

    init {
        addCssStyle(timelineItemStyle)
        addCssStyle(timelineItemStyleAfter)
        addCssStyle(timelineItemLastChildStyle)
    }

}
