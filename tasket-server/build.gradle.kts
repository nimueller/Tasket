plugins {
    kotlin("jvm")
    id("tasket.shared")
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
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content)
    implementation(libs.ktor.server.json)
}
