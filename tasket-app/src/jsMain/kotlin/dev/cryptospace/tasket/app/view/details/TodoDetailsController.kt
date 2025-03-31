package dev.cryptospace.tasket.app.view.details

import dev.cryptospace.tasket.app.network.HttpClient
import dev.cryptospace.tasket.app.network.post
import dev.cryptospace.tasket.payloads.todo.request.TodoCommentRequestPayload
import io.kvision.core.onClickLaunch

class TodoDetailsController(todoId: String) {

    var todoId: String = todoId
        set(value) {
            field = value
        }

    val view = TodoDetailsView()

    init {
        view.saveButton.onClickLaunch {
            val comment = view.commentInput.value ?: return@onClickLaunch
            HttpClient.post("/rest/todos/$todoId/comments", TodoCommentRequestPayload(comment)).handleStatusCodes()
            view.commentInput.value = ""
        }
    }
}
