package dev.cryptospace.tasket.payloads.todo.request

import dev.cryptospace.tasket.payloads.OptionalField
import dev.cryptospace.tasket.payloads.PatchRequestPayload
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class TodoPatchRequestPayload(
    @Contextual val label: OptionalField<String> = OptionalField.Missing,
    @Contextual val statusId: OptionalField<String> = OptionalField.Missing
) : PatchRequestPayload
