package dev.cryptospace.tasket.payloads.todo.response

import dev.cryptospace.tasket.payloads.MetaInformationPayload
import dev.cryptospace.tasket.payloads.ResponsePayload
import kotlinx.serialization.Serializable

@Serializable
data class TodoCommentResponsePayload(
    override val metaInformation: MetaInformationPayload,
    val comment: String
) : ResponsePayload
