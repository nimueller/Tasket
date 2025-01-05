package dev.cryptospace.tasket.payloads.todo.response

import dev.cryptospace.tasket.payloads.MetaInformationPayload
import dev.cryptospace.tasket.payloads.ResponsePayload
import dev.cryptospace.tasket.types.BootstrapColor
import kotlinx.serialization.Serializable

@Serializable
data class TodoStatusResponsePayload(
    override val metaInformation: MetaInformationPayload,
    val name: String,
    val color: BootstrapColor,
) : ResponsePayload
