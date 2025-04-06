package dev.cryptospace.tasket.app.view.details

import dev.cryptospace.tasket.app.components.TimelineItem
import dev.cryptospace.tasket.payloads.todo.response.TodoCommentResponsePayload
import external.DomPurify
import external.Marked
import io.kvision.core.onClickLaunch
import io.kvision.html.Button
import io.kvision.html.ButtonStyle
import io.kvision.html.div

class TodoCommentItem(todoComment: TodoCommentResponsePayload) : TimelineItem(todoComment.metaInformation.createdAt) {

    val deleteButton = Button(text = "", icon = "fas fa-trash", ButtonStyle.OUTLINEDANGER)

    init {
        header.div(className = "float-end") {
            add(this@TodoCommentItem.deleteButton)
        }

        val renderedMarkdown = Marked.parse(todoComment.comment)
        val purifiedMarkdown = DomPurify.sanitize(renderedMarkdown)
        div(content = purifiedMarkdown, rich = true)
    }

    fun onDeleteLaunch(handler: suspend () -> Unit) {
        deleteButton.onClickLaunch {
            handler()
        }
    }
}
