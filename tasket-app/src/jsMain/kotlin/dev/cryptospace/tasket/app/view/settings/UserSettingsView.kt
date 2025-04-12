package dev.cryptospace.tasket.app.view.settings

import dev.cryptospace.tasket.app.components.PasswordValidations
import dev.cryptospace.tasket.app.components.iconFormGroup
import io.kvision.form.text.Password
import io.kvision.form.text.Text
import io.kvision.form.text.password
import io.kvision.html.Div
import io.kvision.html.h2
import io.kvision.html.h4
import io.kvision.i18n.tr

class UserSettingsView : Div(className = "container") {

    val onPasswordChanged = mutableListOf<PasswordChangedHandler>()

    fun interface PasswordChangedHandler {
        fun onPasswordChanged(new: String)
    }

    init {
        h2(content = tr("Settings")) {
            addCssClass("my-3")
        }

        h4(content = tr("Change password")) {
            addCssClass("my-3")
        }
    }

    val currentPassword = passwordField(tr("Current password"))
    val newPassword = passwordField(tr("New password"))
    val repeatNewPassword = passwordField(tr("Repeat new password"))
    val passwordValidations = PasswordValidations().also {
        addCssClass("collapse")
        add(it)
    }

    private fun passwordField(placeholderTitle: String): Password {
        val passwordField = password {
            refreshFieldCssClasses()
            placeholder = placeholderTitle
        }

        iconFormGroup(iconClass = "fas fa-lock") {
            add(passwordField)
        }

        return passwordField
    }

    private fun Text.refreshFieldCssClasses() {
        addCssClass("w-100")
        removeCssClass("kv-mb-3") // stupid preset by KVision. This should be on the row div instead
    }
}
