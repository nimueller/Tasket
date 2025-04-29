@file:JsModule("sortablejs")
@file:JsNonModule

package external

import org.w3c.dom.HTMLElement

external object Sortable {
    fun create(
        @Suppress("unused") element: HTMLElement,
        @Suppress("unused") options: SortableOptions = definedExternally,
    )
}

external interface SortableOptions {
    var animation: Int?
    var onEnd: ((event: Event) -> Unit)?
}

external interface Event {

    var item: HTMLElement?
    var oldIndex: Int
    var newIndex: Int
}
