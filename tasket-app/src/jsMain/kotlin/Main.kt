import dev.cryptospace.tasket.app.App
import external.Sortable
import io.kvision.BootstrapCssModule
import io.kvision.BootstrapModule
import io.kvision.CoreModule
import io.kvision.FontAwesomeModule
import io.kvision.module
import io.kvision.startApplication

fun main() {
    printSortableVersion()
    startApplication({ App }, module.hot, CoreModule, BootstrapModule, BootstrapCssModule, FontAwesomeModule)
}

fun printSortableVersion() {
    println("Version: " + Sortable.version)
}
