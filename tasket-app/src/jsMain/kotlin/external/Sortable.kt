@file:JsModule("sortablejs")
@file:JsNonModule

package external

import org.w3c.dom.HTMLElement

external object Sortable {
    val version: String

    fun create(
        element: HTMLElement,
        options: dynamic = definedExternally,
    )
}

external interface SortableOptions {
    var animation: Int?
    var ghostClass: String?
}
