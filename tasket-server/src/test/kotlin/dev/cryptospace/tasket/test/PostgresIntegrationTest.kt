package dev.cryptospace.tasket.test

import liquibase.Contexts
import liquibase.LabelExpression
import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.testcontainers.containers.PostgreSQLContainer
import java.sql.DriverManager

class PostgresIntegrationTest :
    BeforeEachCallback,
    BeforeAllCallback {

    override fun beforeAll(context: ExtensionContext) {
        if (postgresTestContainer.isRunning()) {
            return
        }

        println("Starting Postgres container")
        startPostgresContainer()
        connectToDatabase()
        runLiquibaseMigrations()
        println("Postgres container started")
    }

    private fun startPostgresContainer() {
        postgresTestContainer.apply {
            withDatabaseName("test")
            withUsername("test")
            withPassword("test")
            start()
        }
    }

    private fun connectToDatabase() {
        println("Connecting to Postgres container")
        Database.connect(
            url = postgresTestContainer.jdbcUrl,
            driver = "org.postgresql.Driver",
            user = postgresTestContainer.username,
            password = postgresTestContainer.password,
        )
    }

    private fun runLiquibaseMigrations() {
        DriverManager.getConnection(
            postgresTestContainer.jdbcUrl,
            postgresTestContainer.username,
            postgresTestContainer.password,
        ).use { connection ->
            val liquibase = Liquibase(
                "liquibase/changelog.xml",
                ClassLoaderResourceAccessor(),
                JdbcConnection(connection),
            )
            liquibase.update(Contexts(), LabelExpression())
        }
    }

    override fun beforeEach(context: ExtensionContext?) {
        println("Cleanup database for test ${context?.displayName}")
        transaction {
            exec("TRUNCATE TABLE tasket.todos CASCADE")
        }
    }

    companion object {
        @JvmStatic
        val postgresTestContainer = PostgreSQLContainer("postgres:latest")
    }
}
