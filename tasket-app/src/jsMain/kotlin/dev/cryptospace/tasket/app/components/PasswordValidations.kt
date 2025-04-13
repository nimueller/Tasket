package dev.cryptospace.tasket.app.components

import dev.cryptospace.tasket.logic.SecurePasswordValidator
import io.kvision.html.Div
import io.kvision.html.span
import io.kvision.i18n.tr

class PasswordValidations : Div() {

    val tooShort =
        iconLabelComponent(
            body = span(tr("Password length must be at least 8 characters long"))
        )
    val noUppercase =
        iconLabelComponent(
            body = span(tr("Password must contain at least one uppercase letter (A-Z)"))
        )
    val noLowercase =
        iconLabelComponent(
            body = span(tr("Password must contain at least one lowercase letter (a-z)"))
        )
    val noDigit =
        iconLabelComponent(
            body = span(tr("Password must contain at least one digit (0-9)"))
        )
    val noSpecial =
        iconLabelComponent(
            body = span(tr("Password must contain at least one special character (e.g. !@#$%^&*)"))
        )
    val noMatch =
        iconLabelComponent(
            body = span(tr("Password and repeat password must match"))
        )

    fun refresh(validationErrors: List<SecurePasswordValidator.ValidationError>) {
        tooShort.valid()
        noUppercase.valid()
        noLowercase.valid()
        noDigit.valid()
        noSpecial.valid()
        noMatch.valid()

        for (error in validationErrors) {
            when (error) {
                SecurePasswordValidator.ValidationError.TOO_SHORT -> tooShort.invalid()
                SecurePasswordValidator.ValidationError.NO_UPPERCASE -> noUppercase.invalid()
                SecurePasswordValidator.ValidationError.NO_LOWERCASE -> noLowercase.invalid()
                SecurePasswordValidator.ValidationError.NO_NUMBER -> noDigit.invalid()
                SecurePasswordValidator.ValidationError.NO_SPECIAL_CHAR -> noSpecial.invalid()
                SecurePasswordValidator.ValidationError.NO_MATCH -> noMatch.invalid()
            }
        }
    }

    private fun IconLabelComponent<*>.valid() {
        icon = "fa fa-check"
        removeCssClass("text-danger")
        addCssClass("text-success")
    }

    private fun IconLabelComponent<*>.invalid() {
        icon = "fa fa-exclamation-triangle"
        removeCssClass("text-success")
        addCssClass("text-danger")
    }

}
