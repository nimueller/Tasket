package external

fun <T> jsonObject(values: T.() -> Unit = {}): T {
    return js("{}").unsafeCast<T>().apply(values)
}
