@file:JsModule("marked")
@file:JsNonModule

package external

import kotlinx.serialization.json.JsonArray

@JsName("marked")
external object Marked {

    fun use(options: dynamic)

    fun parse(@Suppress("unused") value: String): String
}

external interface MarkedOptions {
    var extensions: JsonArray
}
