plugins {
    kotlin("multiplatform")
    id("tasket.shared")
}

kotlin {
    js {
        browser {
            testTask {
                useKarma {
                    useFirefox()
                }
            }
        }
    }
    jvm()
}

dependencies {
    commonMainImplementation(libs.kotlinx.serialization.json)

    commonTestImplementation(libs.kotlin.test)
}
