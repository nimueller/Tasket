@file:JsModule("sortablejs")
@file:JsNonModule

package external

import org.w3c.dom.HTMLElement

external object Sortable {
    val version: String

    fun create(
        @Suppress("unused") element: HTMLElement,
        @Suppress("unused") options: SortableOptions = definedExternally
    )
}

external interface SortableOptions {
    var animation: Int?
}
