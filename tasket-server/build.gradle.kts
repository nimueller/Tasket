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
    implementation(libs.postgresql)
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.exposed)
    implementation("io.ktor:ktor-server-cors-jvm:3.0.0")
}

tasks.named<JavaExec>("run") {
    environment(env.allVariables())
}
