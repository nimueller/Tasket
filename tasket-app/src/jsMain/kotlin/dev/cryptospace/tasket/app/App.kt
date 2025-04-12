package dev.cryptospace.tasket.app

import dev.cryptospace.tasket.app.model.TodoStatusModel
import dev.cryptospace.tasket.app.view.dashboard.headerStyle
import dev.cryptospace.tasket.app.view.login.login
import dev.cryptospace.tasket.app.view.settings.UserSettingsController
import dev.cryptospace.tasket.app.view.todos.TodoListController
import io.kvision.Application
import io.kvision.core.Cursor
import io.kvision.core.Style
import io.kvision.html.ButtonStyle
import io.kvision.html.button
import io.kvision.html.div
import io.kvision.html.h3
import io.kvision.html.link
import io.kvision.i18n.DefaultI18nManager
import io.kvision.i18n.I18n
import io.kvision.panel.root
import io.kvision.routing.Routing
import io.kvision.theme.Theme
import io.kvision.theme.ThemeManager
import io.kvision.theme.themeSwitcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
            ),
        )

        root(id = "root") {
            GlobalScope.launch {
                routing = Routing.init(root = "/", useHash = false)
                TodoStatusModel.init()
                div(className = "p-3") {
                    div(className = "clearfix my-3") {
                        link(label = "") {
                            addCssStyle(Style {
                                cursor = Cursor.POINTER
                            })
                            h3(content = "TASKET", className = "float-start") {
                                addCssStyle(headerStyle)
                            }
                            onClick {
                                routing.navigate("/")
                            }
                        }
                        div(className = "float-end") {
                            themeSwitcher(style = ButtonStyle.OUTLINESECONDARY)
                            button(
                                text = "",
                                icon = "fas fa-cog",
                                style = ButtonStyle.OUTLINESECONDARY,
                                className = "mx-3"
                            ) {
                                onClick {
                                    routing.navigate("/settings")
                                }
                            }
                        }
                    }
                    div {
                        routing.on("/", {
                            removeAll()
                            val controller = TodoListController()
                            add(controller.view)
                        })
                        routing.on("/settings", {
                            removeAll()
                            val controller = UserSettingsController()
                            add(controller.view)
                        })
                    }
                }
                routing.on("/login", {
                    removeAll()
                    login()
                })
                routing.resolve()
            }
        }
    }
}
