rootProject.name = "Tasket"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("ktor", "3.0.0")
            version("logback", "1.5.11")
            library("ktor-server-core", "io.ktor", "ktor-server-core").versionRef("ktor")
            library("ktor-server-netty", "io.ktor", "ktor-server-netty").versionRef("ktor")
            library("ktor-server-content", "io.ktor", "ktor-server-content-negotiation").versionRef("ktor")
            library("ktor-server-json", "io.ktor", "ktor-serialization-kotlinx-json").versionRef("ktor")
            library("logback", "ch.qos.logback", "logback-classic").versionRef("logback")
        }
    }
}

include(":tasket-app")
include(":tasket-server")
include(":tasket-shared")
