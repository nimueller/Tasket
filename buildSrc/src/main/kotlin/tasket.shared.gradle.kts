plugins {
    kotlin("plugin.power-assert")
    kotlin("plugin.serialization")
    id("org.jlleitschuh.gradle.ktlint")
    id("se.solrike.sonarlint")
    id("io.gitlab.arturbosch.detekt")
    jacoco
}

repositories {
    mavenCentral()
}
