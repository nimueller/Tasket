package dev.cryptospace.tasket.server.utils

import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun OffsetDateTime.toIso8601String(): String {
    return atZoneSameInstant(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME)
}
