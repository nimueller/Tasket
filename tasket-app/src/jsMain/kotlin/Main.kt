import io.kvision.BootstrapCssModule
import io.kvision.BootstrapModule
import io.kvision.CoreModule
import io.kvision.module
import io.kvision.startApplication

fun main() {
    startApplication(::App, module.hot, CoreModule, BootstrapModule, BootstrapCssModule)
}
