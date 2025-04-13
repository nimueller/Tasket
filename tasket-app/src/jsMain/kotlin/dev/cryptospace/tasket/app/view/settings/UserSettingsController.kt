package dev.cryptospace.tasket.app.view.settings

import dev.cryptospace.tasket.app.utils.disable
import dev.cryptospace.tasket.app.utils.enable
import dev.cryptospace.tasket.logic.SecurePasswordValidator
import io.kvision.core.onClick
import io.kvision.core.onInput

class UserSettingsController {

    val view = UserSettingsView()

    init {
        view.passwordValidations.visible = false
        view.newPassword.onClick {
            view.passwordValidations.visible = true
        }
        view.repeatNewPassword.onClick {
            view.passwordValidations.visible = true
        }
        view.newPassword.onInput { refreshValidations() }
        view.repeatNewPassword.onInput { refreshValidations() }
        refreshValidations()
    }

    private fun refreshValidations() {
        val newPassword = view.newPassword.value ?: ""
        val repeatNewPassword = view.repeatNewPassword.value ?: ""
        val validationErrors = SecurePasswordValidator.validate(newPassword, repeatNewPassword)

        if (validationErrors.isEmpty()) {
            view.changePasswordSubmitButton.enable()
        } else {
            view.changePasswordSubmitButton.disable()
        }

        view.passwordValidations.refresh(validationErrors)
    }

}
