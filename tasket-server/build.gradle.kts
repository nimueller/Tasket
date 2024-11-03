plugins {
    kotlin("jvm")
    id("tasket.shared")
    id("org.liquibase.gradle")
    application
}

group = "dev.cryptospace"
version = "1.0.0"

application {
    mainClass.set("dev.cryptospace.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    implementation(projects.tasketShared)
    implementation(libs.logback)
    implementation(libs.postgresql)
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.exposed)

    liquibaseRuntime(libs.liquibase.core)
    liquibaseRuntime(libs.postgresql)
    liquibaseRuntime("info.picocli:picocli:4.7.5")
}

tasks.named<JavaExec>("run") {
    environment(env.allVariables())
}

liquibase {
    activities {
        val main by creating {
            val url = "jdbc:postgresql://${env.fetchOrNull("POSTGRES_HOST")!!}:${env.fetchOrNull("POSTGRES_PORT")!!}/${
                env.fetchOrNull(
                    "POSTGRES_DB",
                )!!
            }"
            println(url)
            arguments =
                mapOf(
                    "changelogFile" to "src/main/resources/liquibase/changelog.xml",
                    "searchPath" to project.projectDir,
                    "url" to url,
                    "username" to env.fetchOrNull("POSTGRES_USER")!!,
                    "password" to env.fetchOrNull("POSTGRES_PASSWORD")!!,
                    "driver" to "org.postgresql.Driver",
                )
        }
    }
    runList = "main"
}

tasks.named("run") {
    dependsOn(tasks.update)
}
