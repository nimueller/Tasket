plugins {
    kotlin("multiplatform")
    id("tasket.shared")
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
    }
}
