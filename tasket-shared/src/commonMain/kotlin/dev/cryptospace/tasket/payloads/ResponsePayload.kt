package dev.cryptospace.tasket.payloads

import kotlinx.serialization.Serializable

interface ResponsePayload {
    val metaInformation: MetaInformationPayload

    @Serializable
    object Empty : ResponsePayload {
        override val metaInformation: MetaInformationPayload = MetaInformationPayload(
            id = "empty",
            createdAt = "empty",
            updatedAt = "empty",
            ownerId = "empty",
        )
    }
}
