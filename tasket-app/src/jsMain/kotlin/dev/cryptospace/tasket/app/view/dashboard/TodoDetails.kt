package dev.cryptospace.tasket.app.view.dashboard

import dev.cryptospace.tasket.app.network.HttpClient
import dev.cryptospace.tasket.app.network.get
import dev.cryptospace.tasket.app.utils.fromIso8601String
import dev.cryptospace.tasket.payloads.todo.response.TodoCommentResponsePayload
import dev.cryptospace.tasket.payloads.todo.response.TodoResponsePayload
import io.kvision.badge.Badge
import io.kvision.core.JustifyContent
import io.kvision.core.Style
import io.kvision.form.text.RichText
import io.kvision.modal.Modal
import io.kvision.modal.ModalSize

object TodoDetails {
    private val createdAtBadge = Badge(content = " ")
    private val updatedAtBadge = Badge(content = " ")

    val modal =
        Modal(caption = "", size = ModalSize.XLARGE, centered = true, scrollable = true) {
            footer.add(createdAtBadge)
            footer.add(updatedAtBadge)
            footer.addCssClass("placeholder-glow")
            footer.addCssStyle(
                Style {
                    justifyContent = JustifyContent.SPACEBETWEEN
                },
            )
        }

    suspend fun refreshModal(id: String) {
        setTimestampBadgesInPlaceholderMode()

        val todoPayload = HttpClient.get<TodoResponsePayload>("/rest/todos/$id").handleStatusCodes()
        val todoComments =
            HttpClient.get<List<TodoCommentResponsePayload>>("/rest/todos/$id/comments").handleStatusCodes()

        modal.removeAll()

        todoComments?.forEach { comment ->
            modal.add(RichText(value = comment.comment))
        }

        displayTimestampBadges(todoPayload)
    }

    private fun setTimestampBadgesInPlaceholderMode() {
        createdAtBadge.addCssClass("placeholder")
        createdAtBadge.addCssClass("py-3")
        updatedAtBadge.addCssClass("placeholder")
        updatedAtBadge.addCssClass("py-3")
    }

    private fun displayTimestampBadges(todo: TodoResponsePayload?) {
        createdAtBadge.removeCssClass("placeholder")
        createdAtBadge.removeCssClass("py-3")
        updatedAtBadge.removeCssClass("placeholder")
        updatedAtBadge.removeCssClass("py-3")
        createdAtBadge.content = "Created at ${todo?.metaInformation?.createdAt?.fromIso8601String()}"
        updatedAtBadge.content = "Updated at ${todo?.metaInformation?.updatedAt?.fromIso8601String()}"
        createdAtBadge.refresh()
        updatedAtBadge.refresh()
    }
}
