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
        implementation(libs.kvision.core)
        implementation(libs.kvision.bootstrap)
        implementation(libs.kvision.i18n)
    }
}
