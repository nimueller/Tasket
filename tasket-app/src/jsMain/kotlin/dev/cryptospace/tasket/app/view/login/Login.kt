package dev.cryptospace.tasket.app.view.login

import dev.cryptospace.tasket.app.App
import dev.cryptospace.tasket.app.components.card
import dev.cryptospace.tasket.app.components.iconFormGroup
import dev.cryptospace.tasket.app.model.LoginDataModel
import dev.cryptospace.tasket.app.network.HttpClient
import dev.cryptospace.tasket.app.network.HttpClient.login
import dev.cryptospace.tasket.app.utils.onEnterKeyUp
import io.kvision.core.Container
import io.kvision.core.FlexDirection
import io.kvision.core.JustifyContent
import io.kvision.core.onClickLaunch
import io.kvision.form.FormPanel
import io.kvision.form.check.checkBox
import io.kvision.form.formPanel
import io.kvision.form.text.Text
import io.kvision.form.text.password
import io.kvision.form.text.text
import io.kvision.html.Div
import io.kvision.html.button
import io.kvision.html.div
import io.kvision.html.span
import io.kvision.i18n.tr
import io.kvision.panel.FlexPanel
import io.kvision.panel.flexPanel

fun Container.login() {
    div(className = "d-flex align-items-center vh-100") {
        div(className = "container") {
            flexPanel(direction = FlexDirection.ROW, justify = JustifyContent.CENTER) {
                loginCard()
            }
        }
    }
}

private fun FlexPanel.loginCard() = card(title = tr("Login"), className = "col-4") {
    div(className = "mb-4") {
        span(content = tr("Please enter your username and password"))
    }
    val form = loginForm()
    loginButton(form)
}

private fun Div.loginForm() = formPanel<LoginDataModel> {
    usernameField(form = this)
    passwordField(form = this)

    div(className = "mb-4") {
        div(className = "dropdown-divider")
    }

    rememberMeCheckbox()

    validatorMessage = { tr("Login attempt was unsuccessful. Please check your credentials and try again.") }
    validator = {
        println(it.getData().loginSuccess)
        it.getData().loginSuccess
    }
}

private fun FormPanel<LoginDataModel>.usernameField(form: FormPanel<LoginDataModel>) =
    iconFormGroup(iconClass = "fas fa-user") {
        text {
            refreshFieldCssClasses()
            placeholder = tr("Enter your username")
            onEnterKeyUp { submit(form) }
        }.bind(LoginDataModel::username)
    }

private fun FormPanel<LoginDataModel>.passwordField(form: FormPanel<LoginDataModel>) =
    iconFormGroup(iconClass = "fas fa-lock") {
        password {
            refreshFieldCssClasses()
            placeholder = tr("Enter your password")
            onEnterKeyUp { submit(form) }
        }.bind(LoginDataModel::password)
    }

private fun Text.refreshFieldCssClasses() {
    addCssClass("w-100")
    removeCssClass("kv-mb-3") // stupid preset by KVision. This should be on the row div instead
}

private fun FormPanel<LoginDataModel>.rememberMeCheckbox() = div(className = "mb-4") {
    checkBox(
        label = tr("Remember me"),
        value = true,
    ).bind(LoginDataModel::rememberMe)
}

fun Container.loginButton(form: FormPanel<LoginDataModel>) = div(className = "col") {
    button(tr("Login"), className = "btn btn-primary w-100") {
        setAttribute("type", "submit")

        onClickLaunch {
            submit(form)
        }
    }
}

private suspend fun submit(form: FormPanel<LoginDataModel>) {
    form.clearValidation()
    val successfulLogin = HttpClient.login(form.getData().toPayload())
    form.getData().loginSuccess = successfulLogin

    if (successfulLogin) {
        App.routing.navigate("/")
    } else {
        form.validate()
    }
}
