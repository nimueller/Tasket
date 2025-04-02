package dev.cryptospace.tasket.app.view.details

import dev.cryptospace.tasket.app.network.HttpClient
import dev.cryptospace.tasket.app.network.delete
import dev.cryptospace.tasket.app.network.get
import dev.cryptospace.tasket.app.network.post
import dev.cryptospace.tasket.app.utils.fromIso8601String
import dev.cryptospace.tasket.payloads.MetaInformationPayload
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
            val comment = view.getCommentInput()
            val payload = TodoCommentRequestPayload(comment)
            HttpClient.post("/rest/todos/$todoId/comments", payload).handleStatusCodes()
            view.clearCommentInput()
            refreshItems()
        }
    }

    fun refreshItems() {
        view.comments.clearItems()

        KVScope.launch {
            val result =
                HttpClient.get<List<TodoCommentResponsePayload>>("/rest/todos/$todoId/comments").handleStatusCodes()

            result?.forEach { (metaInformation, comment) ->
                addNewItem(metaInformation, comment)
            }
        }
    }

    private fun addNewItem(metaInformation: MetaInformationPayload, comment: String) {
        view.comments.item(timestamp = metaInformation.createdAt.fromIso8601String()) {
            val renderedMarkdown = marked.parse(comment)
            val purifiedMarkdown = sanitizeHtml(renderedMarkdown)
            div(content = purifiedMarkdown, rich = true)

            onDeleteLaunch {
                HttpClient.delete("/rest/todos/$todoId/comments/${metaInformation.id}").handleStatusCodes()
                refreshItems()
            }
        }
    }
}
