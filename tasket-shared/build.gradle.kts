plugins {
    kotlin("multiplatform")
    id("tasket.shared")
}

kotlin {
    js().browser()
    jvm()
}

dependencies {
    commonMainImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
}
