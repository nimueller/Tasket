import dev.cryptospace.tasket.app.App
import external.HighlightJs
import external.Marked
import external.jsonObject
import external.markedHighlight
import io.kvision.BootstrapCssModule
import io.kvision.BootstrapModule
import io.kvision.CoreModule
import io.kvision.FontAwesomeModule
import io.kvision.module
import io.kvision.startApplication
import kotlin.js.json

val markedHighlightOptions = json(
    "emptyLangClass" to "hljs",
    "langPrefix" to "hljs language-",
    "highlight" to { code: dynamic, lang: dynamic, _: dynamic ->
        val requestedLang = if (lang == null || HighlightJs.getLanguage(lang) == null) {
            "plaintext"
        } else {
            lang
        }
        print("Using language: $requestedLang for code: $code")
        HighlightJs.highlight(code, jsonObject { language = requestedLang }).value
    }
).also {
    println(JSON.stringify(it))
}

fun main() {
    val markedHighlightExtension = markedHighlight(markedHighlightOptions)
    println(JSON.stringify(markedHighlightExtension))
    val options = json("extensions" to markedHighlightExtension)
    println(JSON.stringify(options))

    Marked.use(markedHighlightExtension)

    println(Marked.parse("# Hello world\n\n```kotlin\nval x = 1\n```"))

    startApplication({ App }, module.hot, CoreModule, BootstrapModule, BootstrapCssModule, FontAwesomeModule)
}
