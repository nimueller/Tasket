import dev.cryptospace.tasket.app.App
import dev.cryptospace.tasket.app.model.TodoStatusModel
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
)

fun main() {
    val markedHighlightExtension = markedHighlight(markedHighlightOptions)
    Marked.use(markedHighlightExtension)
    GlobalScope.launch {
        TodoStatusModel.init()
        startApplication({ App }, module.hot, CoreModule, BootstrapModule, BootstrapCssModule, FontAwesomeModule)
    }
}
