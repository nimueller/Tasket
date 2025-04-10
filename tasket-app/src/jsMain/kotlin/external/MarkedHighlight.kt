@file:JsModule("marked-highlight")
@file:JsNonModule

package external

external fun markedHighlight(options: dynamic): dynamic

external interface MarkedHighlightOptions {
    var emptyLangClass: String
    var langPrefix: String
    var highlight: (code: String, lang: String?, info: dynamic) -> Unit
}
