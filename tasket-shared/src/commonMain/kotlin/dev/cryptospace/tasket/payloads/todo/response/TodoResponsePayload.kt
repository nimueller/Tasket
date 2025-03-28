package dev.cryptospace.tasket.payloads.todo.response

import dev.cryptospace.tasket.payloads.MetaInformationPayload
import dev.cryptospace.tasket.payloads.ResponsePayload
import kotlinx.serialization.Serializable

@Serializable
data class TodoResponsePayload(
    override val metaInformation: MetaInformationPayload,
    val label: String,
    val statusId: String,
    val ownerId: String,
) : ResponsePayload
