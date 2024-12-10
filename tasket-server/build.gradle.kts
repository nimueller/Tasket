@file:Suppress("OPT_IN_USAGE")

plugins {
    kotlin("jvm")
    id("tasket.shared")
    id("org.liquibase.gradle")
    id("com.gradleup.shadow") version "8.3.+"
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
    implementation(libs.bouncycastle)
    implementation(libs.bundles.ktor.server)
    implementation(libs.bundles.exposed)
    implementation(libs.bundles.liquibase)

    liquibaseRuntime(libs.bundles.liquibase)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.junit.params)
    testImplementation(libs.bundles.ktor.test)
    testImplementation(libs.bundles.testcontainers)
}

tasks.named<JavaExec>("run") {
    environment(env.allVariables())
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
}

tasks.test {
    environment("JWT_SECRET", "test")
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
    }
}

liquibase {
    activities {
        val main by creating {
            val url = "jdbc:postgresql://${env.fetchOrNull("POSTGRES_HOST")}:${env.fetchOrNull("POSTGRES_PORT")}/${
                env.fetchOrNull(
                    "POSTGRES_DB",
                )
            }"
            arguments = mapOf(
                "changelogFile" to "src/main/resources/liquibase/changelog.xml",
                "searchPath" to project.projectDir,
                "url" to url,
                "username" to env.fetchOrNull("POSTGRES_USER"),
                "password" to env.fetchOrNull("POSTGRES_PASSWORD"),
                "driver" to "org.postgresql.Driver",
            )
        }
    }
    runList = "main"
}

powerAssert {
    functions = listOf("kotlin.assert", "kotlin.test.assertTrue", "kotlin.test.assertEquals", "kotlin.test.assertNull")
    includedSourceSets = listOf(sourceSets.test.name)
}
