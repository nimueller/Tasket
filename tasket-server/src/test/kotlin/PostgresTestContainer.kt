import liquibase.Contexts
import liquibase.LabelExpression
import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import org.jetbrains.exposed.sql.Database
import org.testcontainers.containers.PostgreSQLContainer
import java.sql.DriverManager

object PostgresTestContainer : PostgreSQLContainer<PostgresTestContainer>("postgres:latest")

fun setupTestDatabaseWithPostgres() {
    val container = PostgresTestContainer.apply {
        withDatabaseName("test")
        withUsername("test")
        withPassword("test")
        start()
    }
    Database.connect(
        url = container.jdbcUrl,
        driver = "org.postgresql.Driver",
        user = container.username,
        password = container.password,
    )
    runLiquibaseMigrations(container.jdbcUrl, container.username, container.password)
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
