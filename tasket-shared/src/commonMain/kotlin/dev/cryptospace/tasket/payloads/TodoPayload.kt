package dev.cryptospace.tasket.payloads

import kotlinx.serialization.Serializable

@Serializable
class TodoPayload : Payload() {
    var label: String = ""
}
