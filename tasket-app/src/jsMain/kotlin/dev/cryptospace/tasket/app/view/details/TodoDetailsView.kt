package dev.cryptospace.tasket.app.view.details

import dev.cryptospace.tasket.app.components.TimelineItem
import dev.cryptospace.tasket.app.components.timeline
import dev.cryptospace.tasket.app.utils.SmartListRenderer
import dev.cryptospace.tasket.payloads.todo.response.TodoCommentResponsePayload
import external.DomPurify
import external.Marked
import external.Wysimark
import external.createWysimark
import external.jsonObject
import io.kvision.html.Div
import io.kvision.html.button
import io.kvision.html.div
import io.kvision.i18n.tr

class TodoDetailsView : Div(className = "col") {

    val onCommentItemInserted = mutableListOf<(TimelineItem, TodoCommentResponsePayload) -> Unit>()

    private val commentsContainer = timeline()

    val commentsRenderer = SmartListRenderer<TodoCommentResponsePayload>(
        container = commentsContainer,
        itemRenderer = { comment ->
            val item = TimelineItem(comment.metaInformation.createdAt)

            val renderedMarkdown = Marked.parse(comment.comment)
            val purifiedMarkdown = DomPurify.sanitize(renderedMarkdown)
            println("PURIFIED: $purifiedMarkdown")
            item.div(content = purifiedMarkdown, rich = true)

            onCommentItemInserted.forEach { it(item, comment) }

            return@SmartListRenderer item
        }
    )

    val commentInput = div(className = "wysimark-container") {
        addAfterInsertHook {
            val element = this.getElement()
            checkNotNull(element) { "Element not created" }
            this@TodoDetailsView.commentInputWysimark = createWysimark(
                container = element,
                options = jsonObject {
                    initialMarkdown = null
                }
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
