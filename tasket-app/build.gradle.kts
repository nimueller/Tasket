plugins {
    kotlin("multiplatform")
    id("tasket.shared")
}

kotlin {
    js {
        browser()
        binaries.executable()
    }
}


