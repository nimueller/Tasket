package dev.cryptospace.tasket.app.view.details

import dev.cryptospace.tasket.app.model.TodoStatusModel
import dev.cryptospace.tasket.app.network.HttpClient
import dev.cryptospace.tasket.app.network.delete
import dev.cryptospace.tasket.app.network.get
import dev.cryptospace.tasket.app.network.post
import dev.cryptospace.tasket.payloads.todo.request.TodoCommentRequestPayload
import dev.cryptospace.tasket.payloads.todo.response.TodoCommentResponsePayload
import dev.cryptospace.tasket.payloads.todo.response.TodoResponsePayload
import io.kvision.core.KVScope
import io.kvision.core.onClickLaunch
import kotlinx.coroutines.launch

class TodoDetailsController(todo: TodoResponsePayload) {

    var todo: TodoResponsePayload = todo
        set(value) {
            field = value
            KVScope.launch { refreshComments() }
        }
    private val todoId: String
        get() = todo.metaInformation.id

    val view = TodoDetailsView()

    init {
        view.statusSelectionBox.apply {
            options = TodoStatusModel.statuses.map { status ->
                status.metaInformation.id to status.name
            }
            value = todo.metaInformation.id
        }

        view.onCommentItemInserted += { item, comment ->
            item.onDeleteLaunch {
                HttpClient.delete("/rest/todos/$todoId/comments/${comment.metaInformation.id}").handleStatusCodes()
                refreshComments()
            }
        }

        view.saveButton.onClickLaunch {
            val comment = view.getCommentInput()
            val payload = TodoCommentRequestPayload(comment)
            HttpClient.post("/rest/todos/$todoId/comments", payload).handleStatusCodes()
            view.clearCommentInput()
            refreshComments()
        }
    }

    suspend fun refreshComments() {
        val result =
            HttpClient.get<List<TodoCommentResponsePayload>>("/rest/todos/$todoId/comments").handleStatusCodes()

        if (result != null) {
            view.commentsRenderer.update(result)
        }
    }
}
