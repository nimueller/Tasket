package dev.cryptospace.tasket.app.view.dashboard

import dev.cryptospace.tasket.app.model.TodoViewModel
import dev.cryptospace.tasket.app.network.HttpClient
import dev.cryptospace.tasket.app.network.post
import dev.cryptospace.tasket.payloads.todo.request.TodoRequestPayload
import io.kvision.core.Container
import io.kvision.core.onEventLaunch
import io.kvision.form.text.textInput
import io.kvision.i18n.tr
import org.w3c.dom.events.KeyboardEvent

fun Container.todoInserter() {
    textInput {
        placeholder = tr("New TODO")
        onEventLaunch("keyup") { event ->
            if (event is KeyboardEvent && event.key == "Enter" && !value.isNullOrBlank()) {
                val payload = TodoRequestPayload(label = value!!, statusId = null)
                val success = HttpClient.post("/rest/todos", payload).handleStatusCodes() != null
                if (success) {
                    value = ""
                    TodoViewModel.loadTodos()
                }
            }
        }
    }
}
