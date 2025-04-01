package dev.cryptospace.tasket.app.view.details

import dev.cryptospace.tasket.app.network.HttpClient
import dev.cryptospace.tasket.app.network.get
import dev.cryptospace.tasket.app.network.post
import dev.cryptospace.tasket.app.utils.fromIso8601String
import dev.cryptospace.tasket.payloads.todo.request.TodoCommentRequestPayload
import dev.cryptospace.tasket.payloads.todo.response.TodoCommentResponsePayload
import external.marked
import external.sanitizeHtml
import io.kvision.core.KVScope
import io.kvision.core.onClickLaunch
import io.kvision.html.div
import kotlinx.coroutines.launch

class TodoDetailsController(todoId: String) {

    var todoId: String = todoId
        set(value) {
            field = value
            refreshItems()
        }

    val view = TodoDetailsView()

    init {
        view.saveButton.onClickLaunch {
            val comment = view.commentInput.value ?: return@onClickLaunch
            val payload = TodoCommentRequestPayload(comment)
            HttpClient.post("/rest/todos/$todoId/comments", payload).handleStatusCodes()
            view.commentInput.value = ""
            println("Pre refresh")
            refreshItems()
        }
    }

    fun refreshItems() {
        println("Refresh")
        view.comments.clearItems()

        KVScope.launch {
            val result =
                HttpClient.get<List<TodoCommentResponsePayload>>("/rest/todos/$todoId/comments").handleStatusCodes()

            result?.forEach { commentPayload ->
                view.comments.item(timestamp = commentPayload.metaInformation.createdAt.fromIso8601String()) {
                    val renderedMarkdown = marked.parse(commentPayload.comment)
                    val purifiedMarkdown = sanitizeHtml(renderedMarkdown)
                    div(content = purifiedMarkdown, rich = true)
                }
            }
        }
    }
}
