import io.kvision.Application
import io.kvision.html.span
import io.kvision.panel.root

class App : Application() {
    override fun start() {
        root("root") {
            span("Hello world!")
        }
    }
}
