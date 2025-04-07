@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    kotlin("plugin.power-assert")
    kotlin("plugin.serialization")
//    id("org.jlleitschuh.gradle.ktlint")
    id("se.solrike.sonarlint")
//    id("io.gitlab.arturbosch.detekt")
    jacoco
}

repositories {
    mavenCentral()
}

powerAssert {
    functions = listOf("kotlin.assert", "kotlin.test.assertEquals")
}

//detekt {
//    buildUponDefaultConfig = true
//    config.setFrom(project.rootDir.resolve("detekt.yml"))
//}
//
//tasks.detekt {
//    reports {
//        xml.required.set(true)
//        html.required.set(true)
//        md.required.set(true)
//    }
//}
