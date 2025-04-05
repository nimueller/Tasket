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
                devServer?.port = 8081

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
        implementation(npm("highlight.js", "11.11.1"))
        implementation(npm("marked", "15.0.7"))
        implementation(npm("marked-highlight", "2.2.1"))
        implementation(npm("dompurify", "3.2.5"))
        implementation(npm("@wysimark/standalone", "3.0.20"))
    }
}
