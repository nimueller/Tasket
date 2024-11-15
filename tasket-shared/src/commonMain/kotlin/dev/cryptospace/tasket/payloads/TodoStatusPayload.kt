package dev.cryptospace.tasket.payloads

import dev.cryptospace.tasket.types.BootstrapColor
import kotlinx.serialization.Serializable

@Serializable
data class TodoStatusPayload(val name: String, val color: BootstrapColor) : Payload()
