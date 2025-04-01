@file:JsModule("marked")
@file:JsNonModule

package external

external object marked {
    fun parse(value: String): String
}
