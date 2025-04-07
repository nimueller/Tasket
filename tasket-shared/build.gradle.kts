plugins {
    kotlin("multiplatform")
    id("tasket.shared")
}

kotlin {
    js {
        browser {
        }
    }
    jvm {
        dependencies {
            commonTestImplementation(libs.kotlin.test)
        }
    }
}

dependencies {
    commonMainImplementation(libs.kotlinx.serialization.json)
}
