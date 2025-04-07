package dev.cryptospace.tasket.server.payload

import dev.cryptospace.tasket.payloads.RequestPayload
import dev.cryptospace.tasket.server.table.BaseTable

fun interface PatchRequestMapper<T : BaseTable, P : RequestPayload> : RequestMapper<T, P>
