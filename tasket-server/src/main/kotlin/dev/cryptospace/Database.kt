package dev.cryptospace

import liquibase.Contexts
import liquibase.LabelExpression
import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.jetbrains.exposed.sql.Schema
import org.jetbrains.exposed.sql.Slf4jSqlDebugLogger
import org.postgresql.Driver
import java.sql.DriverManager

fun initializeDatabase() {
    val databaseUrl = buildDatabaseUrl()
    val username = System.getenv("POSTGRES_USER") ?: "postgres"
    val password = System.getenv("POSTGRES_PASSWORD") ?: "postgres"

    connectToDatabase(databaseUrl, username, password)
    runLiquibaseMigrations(databaseUrl, username, password)
}

private fun buildDatabaseUrl(): String {
    val host = System.getenv("POSTGRES_HOST") ?: "localhost"
    val port = System.getenv("POSTGRES_PORT") ?: "5432"
    val database = System.getenv("POSTGRES_DB") ?: "tasket"

    return "jdbc:postgresql://$host:$port/$database"
}

private fun connectToDatabase(databaseUrl: String, username: String, password: String) {
    Database.connect(
        url = databaseUrl + "?stringtype=unspecified",
        user = username,
        password = password,
        driver = requireNotNull(Driver::class.qualifiedName),
        databaseConfig = DatabaseConfig {
            defaultSchema = Schema("tasket")
            sqlLogger = Slf4jSqlDebugLogger
        },
        setupConnection = { connection ->
            connection.clientInfo["stringtype"] = "unspecified"
        },
    )
}

private fun runLiquibaseMigrations(connectionUrl: String, username: String, password: String) {
    DriverManager.getConnection(connectionUrl, username, password).use { connection ->
        val liquibase = Liquibase(
            "liquibase/changelog.xml",
            ClassLoaderResourceAccessor(),
            JdbcConnection(connection),
        )
        liquibase.update(Contexts(), LabelExpression())
    }
}
