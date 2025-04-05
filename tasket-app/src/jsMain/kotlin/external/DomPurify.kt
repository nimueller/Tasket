package external

@JsModule("dompurify")
@JsNonModule
@JsName("DOMPurify")
external object DomPurify {
    fun sanitize(@Suppress("unused") dirty: String): String
}
