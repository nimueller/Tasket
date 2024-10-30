plugins {
    kotlin("multiplatform")
    id("tasket.shared")
    alias(libs.plugins.kvision)
}

kotlin {
    js(IR) {
        browser {
            commonWebpackConfig {
                outputFileName = "main.js"
                sourceMaps = false

                cssSupport {
                    enabled.set(true)
                }
            }
        }
        binaries.executable()
    }

    sourceSets.jsMain.dependencies {
        implementation(projects.tasketShared)
        implementation(libs.bundles.kvision)
        implementation(npm("sortablejs", "1.15.3"))
        implementation(npm("is-sorted", "1.0.5"))
    }
}
