package dev.cryptospace.tasket.payloads

import kotlinx.serialization.Serializable

@Serializable
data class MetaInformationPayload(
    val id: String,
    val createdAt: String,
    val updatedAt: String,
    val ownerId: String?,
)
