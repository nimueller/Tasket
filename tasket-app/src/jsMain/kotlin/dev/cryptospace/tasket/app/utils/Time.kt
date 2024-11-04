package dev.cryptospace.tasket.app.utils

import kotlin.js.Date
import kotlin.js.Json
import kotlin.js.json

external object Intl {
    class DateTimeFormat(pattern: dynamic, options: Json = definedExternally) {
        fun format(date: Date): String
    }
}

fun String.fromIso8601String(): String {
    val date = Date(this)
    return Intl.DateTimeFormat(
        undefined, json(
            "dateStyle" to "medium",
            "timeStyle" to "medium",
        )
    ).format(date)
}
