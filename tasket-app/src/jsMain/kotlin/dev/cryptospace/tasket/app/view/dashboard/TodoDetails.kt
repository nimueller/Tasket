package dev.cryptospace.tasket.app.view.dashboard

import dev.cryptospace.tasket.app.model.TodoStatusModel
import dev.cryptospace.tasket.app.network.HttpClient
import dev.cryptospace.tasket.app.network.get
import dev.cryptospace.tasket.app.utils.fromIso8601String
import dev.cryptospace.tasket.app.utils.toBackgroundBsColor
import dev.cryptospace.tasket.app.view.details.TodoDetailsController
import dev.cryptospace.tasket.app.view.todos.TodoListController
import dev.cryptospace.tasket.payloads.todo.response.TodoResponsePayload
import io.kvision.badge.Badge
import io.kvision.core.BsColor
import io.kvision.core.JustifyContent
import io.kvision.core.Style
import io.kvision.core.addBsColor
import io.kvision.core.removeBsColor
import io.kvision.i18n.gettext
import io.kvision.modal.Modal
import io.kvision.modal.ModalSize

object TodoDetails {
    private var currentHeaderBadgeColor: BsColor = BsColor.SECONDARYBG
    private val headerBadge = Badge(content = " ", bsColor = currentHeaderBadgeColor) {
        addCssClass("mx-3")
    }
    private val createdAtBadge = Badge(content = " ")
    private val updatedAtBadge = Badge(content = " ")

    val modal =
        Modal(caption = null, size = ModalSize.XLARGE, centered = true, scrollable = true) {
            header.add(1, headerBadge)

            footer.add(createdAtBadge)
            footer.add(updatedAtBadge)
            footer.addCssClass("placeholder-glow")
            footer.addCssStyle(
                Style {
                    justifyContent = JustifyContent.SPACEBETWEEN
                },
            )
        }

    suspend fun refreshModal(todoListController: TodoListController, todoId: String) {
        setTimestampBadgesInPlaceholderMode()

        val todo = HttpClient.get<TodoResponsePayload>("/rest/todos/$todoId").handleStatusCodes()

        if (todo == null) {
            modal.hide()
            return
        }

        modal.removeAll()
        val todoDetails = TodoDetailsController(todoListController, todo)
        todoDetails.refreshComments()
        modal.add(todoDetails.view)

        modal.caption = todo.label
        val status = TodoStatusModel.getStatusById(todo.statusId)
        headerBadge.content = gettext("Status: %1", status.name)
        headerBadge.removeBsColor(currentHeaderBadgeColor)
        headerBadge.addBsColor(status.color.toBackgroundBsColor())
        currentHeaderBadgeColor = status.color.toBackgroundBsColor()

        displayTimestampBadges(todo)
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
