package dev.cryptospace.tasket.app.view.details.item

import dev.cryptospace.tasket.app.components.StatusBadge
import dev.cryptospace.tasket.app.components.TimelineItem
import dev.cryptospace.tasket.app.utils.fromIso8601String
import dev.cryptospace.tasket.payloads.todo.response.TodoStatusChangeResponsePayload
import io.kvision.html.div
import io.kvision.html.span
import io.kvision.i18n.tr

class TodoStatusChangeItem(
    todoStatusChange: TodoStatusChangeResponsePayload
) : TimelineItem(todoStatusChange.metaInformation.createdAt.fromIso8601String()) {

    init {
        div {
            span(content = tr("Changed status from"))
            add(StatusBadge(todoStatusChange.oldStatus))
            span(content = tr("to"))
            add(StatusBadge(todoStatusChange.newStatus))
        }
    }
}
