package dev.cryptospace.tasket.app.components

import io.kvision.core.Background
import io.kvision.core.Border
import io.kvision.core.BorderStyle
import io.kvision.core.Color
import io.kvision.core.Display
import io.kvision.core.ListStyle
import io.kvision.core.ListStyleType
import io.kvision.core.Position
import io.kvision.core.Style
import io.kvision.html.Li
import io.kvision.html.Ul
import io.kvision.utils.perc
import io.kvision.utils.px
import io.kvision.utils.rem

val timelineStyle = Style(".timeline") {
    borderLeft = Border(2.px, BorderStyle.SOLID, Color.rgb(255, 255, 255))
    position = Position.RELATIVE
    listStyle = ListStyle(ListStyleType.NONE)
}

val timelineItemStyle = Style(".timeline-item") {
    position = Position.RELATIVE
    paddingBottom = 2.rem
}

val timelineItemStyleAfter = Style(".timeline-item::after") {
    background = Background(color = Color.rgb(255, 255, 255))
    position = Position.ABSOLUTE
    display = Display.BLOCK
    top = 0.px
    setStyle("left", "calc(-2rem - 7px)")
    borderRadius = 50.perc
    width = 12.px
    height = 12.px
    setStyle("content", "\"\"")
}


class Timeline(init: Timeline.() -> Unit = {}) : Ul() {

    init {
        addCssStyle(timelineStyle)
        init()
    }

    fun item(init: TimelineItem.() -> Unit) {
        this.add(TimelineItem().apply(init))
    }
}

class TimelineItem : Li() {

    init {
        addCssStyle(timelineItemStyle)
        addCssStyle(timelineItemStyleAfter)
    }

}
