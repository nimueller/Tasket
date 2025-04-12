package dev.cryptospace.tasket.app.components

import dev.cryptospace.tasket.app.model.TodoStatusModel
import dev.cryptospace.tasket.app.utils.toBackgroundBsColor
import dev.cryptospace.tasket.payloads.todo.response.TodoStatusResponsePayload
import io.kvision.badge.Badge
import io.kvision.core.BsColor
import io.kvision.core.addBsColor
import io.kvision.core.removeBsColor

class StatusBadge(initialStatus: TodoStatusResponsePayload?) : Badge() {

    private var currentBackgroundColor: BsColor? = null

    var status: TodoStatusResponsePayload? = null
        set(value) {
            content = value?.name
            currentBackgroundColor?.let { removeBsColor(it) }
            value?.let { addBsColor(it.color.toBackgroundBsColor()) }
        }

    constructor(statusId: String) : this(TodoStatusModel.getStatusById(statusId))

    init {
        addCssClass("mx-1")
        content = initialStatus?.name
        status = initialStatus
    }

}
