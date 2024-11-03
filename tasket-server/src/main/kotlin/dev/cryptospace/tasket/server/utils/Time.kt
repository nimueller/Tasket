package dev.cryptospace.tasket.server.utils

import java.time.OffsetDateTime
import java.time.ZoneOffset

fun OffsetDateTime.toUtcMillis(): Long {
    return atZoneSameInstant(ZoneOffset.UTC).toInstant().toEpochMilli()
}

