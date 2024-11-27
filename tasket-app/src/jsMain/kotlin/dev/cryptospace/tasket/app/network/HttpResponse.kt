package dev.cryptospace.tasket.app.network

import dev.cryptospace.tasket.app.App
import io.kvision.i18n.gettext
import io.kvision.i18n.tr
import io.kvision.modal.Alert

data class HttpResponse<T : Any>(val status: Short, val parsedEntity: T?) {
    fun handleStatusCodes(): T? {
        if (parsedEntity != null) {
            return parsedEntity
        }

        if (status == 401.toShort()) {
            App.routing.navigate("/login")
        } else {
            Alert.show(
                caption = tr("Error"),
                text = gettext(
                    """<p>An error occurred. Please try again later.</p>
                    <p>If you are an administrator, please check the server logs and file a bug report at
                    <a href="%1">GitHub</a>.</p>""",
                    "https://github.com/nimueller/Tasket/issues/new",
                ),
                rich = true,
                centered = true,
            )
        }

        return null
    }
}
