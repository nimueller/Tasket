plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation(kotlin("gradle-plugin", version = "2.0.0"))
    implementation(kotlin("power-assert", version = "2.0.0"))
    implementation(kotlin("serialization", version = "2.0.0"))
    implementation("org.jlleitschuh.gradle:ktlint-gradle:12.1.1")
    implementation("se.solrike.sonarlint:sonarlint-gradle-plugin:2.0.0")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.23.7")
}
