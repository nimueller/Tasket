import dev.cryptospace.tasket.app.todoInserter
import dev.cryptospace.tasket.app.todoList
import io.kvision.Application
import io.kvision.core.FlexDirection
import io.kvision.core.Style
import io.kvision.html.ButtonStyle
import io.kvision.html.div
import io.kvision.panel.flexPanel
import io.kvision.panel.root
import io.kvision.theme.Theme
import io.kvision.theme.ThemeManager
import io.kvision.theme.themeSwitcher

class App : Application() {
    init {
        ThemeManager.init(initialTheme = Theme.DARK)
    }

    override fun start() {
        root(id = "root") {
            div(className = "p-3") {
                flexPanel(direction = FlexDirection.ROW) {
                    addCssStyle(
                        Style {
                            setStyle("gap", "1rem")
                        },
                    )
                    todoInserter()
                    themeSwitcher(style = ButtonStyle.OUTLINESECONDARY)
                }
                div(className = "mt-3") {
                    todoList()
                }
            }
        }
    }
}
