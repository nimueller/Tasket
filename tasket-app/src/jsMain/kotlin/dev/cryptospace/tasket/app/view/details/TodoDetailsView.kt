package dev.cryptospace.tasket.app.view.details

import dev.cryptospace.tasket.app.components.timeline
import external.Wysimark
import external.createWysimark
import io.kvision.html.Div
import io.kvision.html.button
import io.kvision.html.div
import io.kvision.i18n.tr
import kotlin.js.json

class TodoDetailsView : Div(className = "col") {

    val comments = timeline()

    val commentInput = div(className = "wysimark-container") {
        addAfterInsertHook {
            val element = this.getElement()
            checkNotNull(element) { "Element not created" }
            this@TodoDetailsView.commentInputWysimark = createWysimark(
                container = element, options = json(
                    "initialMarkdown" to "",
                )
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
