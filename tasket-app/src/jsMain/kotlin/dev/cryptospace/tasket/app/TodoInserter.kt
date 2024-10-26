package dev.cryptospace.tasket.app

import dev.cryptospace.tasket.payloads.TodoPayload
import io.kvision.core.Container
import io.kvision.core.onInput
import io.kvision.form.text.textInput

fun Container.todoInserter() {
    textInput {
        onInput {
            TodoState.todos += TodoPayload("Test")
        }
    }
}
