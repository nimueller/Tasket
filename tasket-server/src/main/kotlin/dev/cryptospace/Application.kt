package dev.cryptospace

import dev.cryptospace.tasket.server.routes.todo
import dev.cryptospace.tasket.server.table.TodosTable
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.jetbrains.exposed.sql.Schema
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
        databaseConfig =
            DatabaseConfig {
                defaultSchema = Schema("tasket")
            },
    )

    transaction {
        SchemaUtils.createMissingTablesAndColumns(TodosTable)
    }

    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        if (System.getenv("ALLOWED_HOST") != null) allowHost(System.getenv("ALLOWED_HOST")) else anyHost()
    }
    routing {
        todo()
    }
}
