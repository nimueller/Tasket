package external

@JsModule("highlight.js")
@JsNonModule
@JsName("hljs")
external object HighlightJs {
    fun getLanguage(
        @Suppress("unused") name: String
    ): Language?

    fun highlight(
        @Suppress("unused") code: String,
        @Suppress("unused") options: HighlightOptions
    ): HighlightResult
}

external interface Language {
    val name: String
}

external interface HighlightOptions {
    var language: String
}

external interface HighlightResult {
    val value: String
}
