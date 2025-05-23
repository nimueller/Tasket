package dev.cryptospace.tasket.app.view.details

import dev.cryptospace.tasket.app.components.timeline
import dev.cryptospace.tasket.app.utils.SmartListRenderer
import dev.cryptospace.tasket.app.view.details.item.TodoCommentItem
import dev.cryptospace.tasket.app.view.details.item.TodoStatusChangeItem
import dev.cryptospace.tasket.payloads.ResponsePayload
import dev.cryptospace.tasket.payloads.todo.response.TodoCommentResponsePayload
import dev.cryptospace.tasket.payloads.todo.response.TodoStatusChangeResponsePayload
import external.Wysimark
import external.createWysimark
import external.jsonObject
import io.kvision.form.select.SelectInput
import io.kvision.html.Div
import io.kvision.html.button
import io.kvision.html.div
import io.kvision.html.span
import io.kvision.i18n.tr

class TodoDetailsView : Div(className = "col") {

    val onCommentItemInserted = mutableListOf<(TodoCommentItem, TodoCommentResponsePayload) -> Unit>()

    val statusSelectionBox = SelectInput(className = "float-end")

    init {
        div(className = "my-3 clearfix") {
            span(content = tr("Status"), className = "float-start")
            add(this@TodoDetailsView.statusSelectionBox)
        }
    }

    private val commentsContainer = timeline()

    val commentsRenderer = SmartListRenderer<ResponsePayload>(
        container = commentsContainer,
        itemRenderer = { response ->
            val item = when (response) {
                is TodoStatusChangeResponsePayload -> TodoStatusChangeItem(response)
                is TodoCommentResponsePayload -> {
                    val item = TodoCommentItem(response)
                    onCommentItemInserted.forEach { it(item, response) }
                    item
                }

                else -> error("Unexpected type of response: $response")
            }
            return@SmartListRenderer item
        },
    )

    val commentInput = div(className = "wysimark-container") {
        addAfterInsertHook {
            val element = this.getElement()
            checkNotNull(element) { "Element not created" }
            this@TodoDetailsView.commentInputWysimark = createWysimark(
                container = element,
                options = jsonObject {
                    initialMarkdown = null
                },
            )
        }
    }

    val saveButton = button(tr("Save"), className = "btn btn-primary mt-3")

    private lateinit var commentInputWysimark: Wysimark

    fun getCommentInput(): String {
        return commentInputWysimark.getMarkdown()
    }

    fun clearCommentInput() {
        commentInputWysimark.setMarkdown("")
    }
}
