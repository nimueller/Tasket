package dev.cryptospace.tasket.app.view.todos

import dev.cryptospace.tasket.app.model.TodoStatusModel
import dev.cryptospace.tasket.app.utils.toBackgroundBsColor
import dev.cryptospace.tasket.payloads.todo.response.TodoResponsePayload
import io.kvision.badge.badge
import io.kvision.core.Cursor
import io.kvision.core.FlexDirection
import io.kvision.core.Style
import io.kvision.form.check.CheckBox
import io.kvision.html.Button
import io.kvision.html.ButtonStyle
import io.kvision.html.Div
import io.kvision.html.div
import io.kvision.html.span
import io.kvision.i18n.gettext
import io.kvision.panel.FlexPanel
import io.kvision.panel.flexPanel
import org.w3c.dom.HTMLElement

val todoListItemHandleStyle = Style {
    cursor = Cursor.POINTER
}

class TodoListItem(
    private val todo: TodoResponsePayload,
) : FlexPanel(className = "list-group-item justify-content-between py-0") {

    private val status = TodoStatusModel.getStatusById(todo.statusId)

    val completeCheckbox = CheckBox {
        addCssClass("my-3")
    }

    val clickHandle = Div(className = "flex-grow-1 py-3 user-select-none") {
        addCssStyle(todoListItemHandleStyle)
        span(className = "list-group-item-text", content = todo.label)
        badge(
            content = gettext("Status: %1", status.name),
            bsColor = status.color.toBackgroundBsColor(),
        ) {
            addCssClass("mx-3")
        }
    }

    val deleteButton = Button(text = "", icon = "fas fa-trash", ButtonStyle.OUTLINEDANGER)

    init {
        flexPanel(FlexDirection.ROW, className = "flex-grow-1") {
            add(completeCheckbox)
            add(clickHandle)
        }
        div(className = "my-3") {
            add(deleteButton)
        }

        addAfterInsertHook { node ->
            val element = node.elm as? HTMLElement
            element?.setAttribute("data-todo-id", todo.metaInformation.id)
        }
    }
}
