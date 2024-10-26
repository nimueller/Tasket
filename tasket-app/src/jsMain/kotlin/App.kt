import dev.cryptospace.tasket.app.todoInserter
import dev.cryptospace.tasket.app.todoList
import io.kvision.Application
import io.kvision.panel.root

class App : Application() {
    override fun start() {
        root("root") {
            todoInserter()
            todoList()
        }
    }
}
