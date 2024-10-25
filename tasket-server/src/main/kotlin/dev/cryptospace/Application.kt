package dev.cryptospace

import dev.cryptospace.tasket.payloads.TestPayload
import dev.cryptospace.tasket.server.routes.todo
import dev.cryptospace.tasket.server.table.Todos
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.Driver

fun buildDatabaseUrl(): String {
    val host = System.getenv("POSTGRES_HOST") ?: "localhost"
    val port = System.getenv("POSTGRES_PORT") ?: "5432"
    val database = System.getenv("POSTGRES_DB") ?: "rss"

    return "jdbc:postgresql://$host:$port/$database"
}

fun main() {
    Database.connect(
        url = buildDatabaseUrl(),
        user = System.getenv("POSTGRES_USER") ?: "postgres",
        password = System.getenv("POSTGRES_PASSWORD") ?: "postgres",
        driver = requireNotNull(Driver::class.qualifiedName),
    )

    transaction {
        SchemaUtils.createMissingTablesAndColumns(Todos)
    }

    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    routing {
        get("/") {
            call.respond(TestPayload(value = "Hello World!"))
        }
        todo()
    }
}
