package dev.cryptospace.tasket.app.view.settings

import io.kvision.core.onClick
import io.kvision.core.onInput

class UserSettingsController {

    val view = UserSettingsView()

    init {
        view.newPassword.onClick {
        }
        view.repeatNewPassword.onClick {
        }
        view.newPassword.onInput { refreshValidations() }
        view.repeatNewPassword.onInput { refreshValidations() }
        refreshValidations()
    }

    private fun refreshValidations() {
        view.passwordValidations.refresh(view.newPassword.value ?: "", view.repeatNewPassword.value ?: "")
    }

}
