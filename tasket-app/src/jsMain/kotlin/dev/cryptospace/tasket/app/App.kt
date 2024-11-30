package dev.cryptospace.tasket.app

import dev.cryptospace.tasket.app.view.dashboard.dashboard
import dev.cryptospace.tasket.app.view.login.login
import io.kvision.Application
import io.kvision.i18n.DefaultI18nManager
import io.kvision.i18n.I18n
import io.kvision.panel.root
import io.kvision.routing.Routing
import io.kvision.theme.Theme
import io.kvision.theme.ThemeManager

object App : Application() {
    lateinit var routing: Routing

    init {
        ThemeManager.init(initialTheme = Theme.DARK)
    }

    override fun start() {
        I18n.manager = DefaultI18nManager(
            mapOf(
                "en" to io.kvision.require("./i18n/messages-en.json"),
                "de" to io.kvision.require("./i18n/messages-de.json"),
            )
        )

        root(id = "root") {
            routing = Routing.init(root = "/", useHash = false)
            routing.on("/", {
                removeAll()
                dashboard()
            })
            routing.on("/login", {
                removeAll()
                login()
            })
            routing.resolve()
        }
    }
}
