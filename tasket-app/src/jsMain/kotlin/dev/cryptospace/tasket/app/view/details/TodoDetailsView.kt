package dev.cryptospace.tasket.app.view.details

import dev.cryptospace.tasket.app.components.timeline
import io.kvision.form.text.textAreaInput
import io.kvision.html.Div
import io.kvision.html.button
import io.kvision.i18n.tr

class TodoDetailsView : Div(className = "col") {

    val comments = timeline()

    val commentInput = textAreaInput {
        placeholder = tr("Add a comment...")
    }

    val saveButton = button(tr("Save"), className = "btn btn-primary mt-3")
}
