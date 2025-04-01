@file:JsModule("@wysimark/standalone")
@file:JsNonModule

package external

import kotlin.js.Json

external fun createWysimark(
    container: org.w3c.dom.Element,
    options: Json
): Wysimark

external class Wysimark {
    fun getMarkdown(): String

    fun setMarkdown(markdown: String)
}
