package dev.cryptospace.tasket.payloads.user.response

import dev.cryptospace.tasket.payloads.MetaInformationPayload
import dev.cryptospace.tasket.payloads.ResponsePayload
import kotlinx.serialization.Serializable

@Serializable
data class UserResponsePayload(
    override val metaInformation: MetaInformationPayload,
    val username: String
) : ResponsePayload
