package dev.cryptospace.tasket.payloads

import kotlinx.serialization.Serializable

@Serializable
abstract class Payload {
    var id: String? = null
    var createdAt: String? = null
    var updatedAt: String? = null
}
