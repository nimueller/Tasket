package dev.cryptospace.tasket.app.view.settings

import dev.cryptospace.tasket.app.model.UserSession
import dev.cryptospace.tasket.app.network.HttpClient
import dev.cryptospace.tasket.app.network.patch
import dev.cryptospace.tasket.app.utils.disable
import dev.cryptospace.tasket.app.utils.enable
import dev.cryptospace.tasket.app.utils.onFocus
import dev.cryptospace.tasket.logic.SecurePasswordValidator
import dev.cryptospace.tasket.payloads.ResponsePayload
import dev.cryptospace.tasket.payloads.user.request.UserChangePasswordRequestPayload
import io.kvision.core.onClickLaunch
import io.kvision.core.onInput

class UserSettingsController {

    val view = UserSettingsView()

    init {
        view.passwordChangeSuccessful.hide()
        view.passwordChangeError.hide()

        view.passwordValidations.visible = false
        view.newPassword.onFocus {
            view.passwordChangeSuccessful.hide()
            view.passwordChangeError.hide()
            view.passwordValidations.visible = true
        }
        view.repeatNewPassword.onFocus {
            view.passwordChangeSuccessful.hide()
            view.passwordChangeError.hide()
            view.passwordValidations.visible = true
        }
        view.newPassword.onInput { refreshValidations() }
        view.repeatNewPassword.onInput { refreshValidations() }
        refreshValidations()

        view.changePasswordSubmitButton.onClickLaunch {
            view.passwordChangeSuccessful.hide()
            view.passwordChangeError.hide()

            val me = UserSession.getMe()
            val payload = UserChangePasswordRequestPayload(
                currentPassword = view.currentPassword.value ?: "",
                newPassword = view.newPassword.value ?: "",
                repeatNewPassword = view.repeatNewPassword.value ?: ""
            )

            val response =
                HttpClient.patch<_, ResponsePayload.Empty>(
                    "/rest/users/${me.metaInformation.id}/change-password",
                    payload
                )

            if (response.status == 200.toShort()) {
                view.currentPassword.value = ""
                view.newPassword.value = ""
                view.repeatNewPassword.value = ""
                view.passwordChangeSuccessful.show()
                view.passwordChangeError.hide()
                view.passwordValidations.hide()
            } else {
                view.passwordChangeSuccessful.hide()
                view.passwordChangeError.show()
            }
        }
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
