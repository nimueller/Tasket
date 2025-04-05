@file:JsModule("@wysimark/standalone")
@file:JsNonModule

package external

import org.w3c.dom.Element

external fun createWysimark(
    container: Element,
    options: WysimarkOptions
): Wysimark

external class Wysimark {
    fun getMarkdown(): String

    fun setMarkdown(@Suppress("unused") markdown: String)
}

external interface WysimarkOptions {
    var initialMarkdown: String?
}
