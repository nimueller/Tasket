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
                sourceMaps = true

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
        implementation(npm("marked", "15.0.7"))
        implementation(npm("sanitize-html", "2.15.0"))
        implementation(npm("@wysimark/standalone", "3.0.20"))
    }
}
