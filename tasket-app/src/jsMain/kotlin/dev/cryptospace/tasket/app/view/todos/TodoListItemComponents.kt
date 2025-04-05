package dev.cryptospace.tasket.app.view.todos

import io.kvision.core.Widget
import io.kvision.form.check.CheckBox
import io.kvision.html.Button

data class TodoListItemComponents(
    val completeCheckbox: CheckBox,
    val clickHandle: Widget,
    val deleteButton: Button,
)
