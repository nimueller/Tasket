package dev.cryptospace.tasket.app.view.details

import dev.cryptospace.tasket.app.model.TodoStatusModel
import dev.cryptospace.tasket.app.network.HttpClient
import dev.cryptospace.tasket.app.network.delete
import dev.cryptospace.tasket.app.network.get
import dev.cryptospace.tasket.app.network.patch
import dev.cryptospace.tasket.app.network.post
import dev.cryptospace.tasket.app.view.todos.TodoListController
import dev.cryptospace.tasket.payloads.OptionalField
import dev.cryptospace.tasket.payloads.todo.request.TodoCommentRequestPayload
import dev.cryptospace.tasket.payloads.todo.request.TodoPatchRequestPayload
import dev.cryptospace.tasket.payloads.todo.response.TodoCommentResponsePayload
import dev.cryptospace.tasket.payloads.todo.response.TodoResponsePayload
import dev.cryptospace.tasket.payloads.todo.response.TodoStatusChangeResponsePayload
import io.kvision.core.KVScope
import io.kvision.core.onChangeLaunch
import io.kvision.core.onClickLaunch
import kotlinx.coroutines.launch

class TodoDetailsController(
    private val todoListController: TodoListController,
    todo: TodoResponsePayload?
) {

    val onStatusChanged = mutableListOf<suspend () -> Unit>()

    var todo: TodoResponsePayload? = todo
        set(value) {
            field = value
            KVScope.launch {
                view.statusSelectionBox.value = value?.statusId
                view.clearCommentInput()
                refreshModificationItems()
            }
        }
    private val todoId: String?
        get() = todo?.metaInformation?.id

    val view = TodoDetailsView()

    init {
        view.statusSelectionBox.apply {
            options = TodoStatusModel.statuses.map { status ->
                status.metaInformation.id to status.name
            }

            onChangeLaunch {
                val selectedStatusId = value
                if (selectedStatusId != null) {
                    val payload = TodoPatchRequestPayload(statusId = OptionalField.Present(selectedStatusId))
                    HttpClient.patch("/rest/todos/$todoId", payload).handleStatusCodes()
                    todoListController.refreshTodos()
                    onStatusChanged.forEach { it() }
                }
            }
        }

        view.onCommentItemInserted += { item, comment ->
            item.onDeleteLaunch {
                HttpClient.delete("/rest/todos/$todoId/comments/${comment.metaInformation.id}").handleStatusCodes()
                refreshModificationItems()
            }
        }

        view.saveButton.onClickLaunch {
            val comment = view.getCommentInput()
            val payload = TodoCommentRequestPayload(comment)
            HttpClient.post("/rest/todos/$todoId/comments", payload).handleStatusCodes()
            view.clearCommentInput()
            refreshModificationItems()
        }
    }

    private suspend fun refreshModificationItems() {
        val id = todo?.metaInformation?.id

        if (id == null) {
            view.commentsRenderer.update(emptyList())
            return
        }

        val statusChanges =
            HttpClient.get<List<TodoStatusChangeResponsePayload>>("/rest/todos/$id/statusChanges").handleStatusCodes()
        val comments = HttpClient.get<List<TodoCommentResponsePayload>>("/rest/todos/$id/comments").handleStatusCodes()

        if (statusChanges != null && comments != null) {
            val items = (statusChanges + comments).sortedBy { it.metaInformation.createdAt }
            view.commentsRenderer.update(items)
        }
    }
}
