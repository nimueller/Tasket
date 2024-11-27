package dev.cryptospace.tasket.app

import dev.cryptospace.tasket.app.view.dashboard.dashboard
import dev.cryptospace.tasket.app.view.login.login
import io.kvision.Application
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
