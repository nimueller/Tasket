package dev.cryptospace.tasket.app

import dev.cryptospace.tasket.app.model.TodoViewModel
import dev.cryptospace.tasket.app.network.HttpClient
import dev.cryptospace.tasket.payloads.TodoPayload
import io.kvision.core.Container
import io.kvision.core.onEventLaunch
import io.kvision.form.text.textInput
import io.kvision.i18n.tr
import org.w3c.dom.events.KeyboardEvent

fun Container.todoInserter() {
    textInput {
        placeholder = tr("New TODO")
        onEventLaunch("keyup") { event ->
            if (event is KeyboardEvent && event.key == "Enter" && value?.isNotBlank() == true) {
                val payload = TodoPayload(label = value ?: "")
                HttpClient.post("todo", payload)
                value = ""
                TodoViewModel.loadTodos()
            }
        }
    }
}
