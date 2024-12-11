buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        // Liquibase itself and the Liquibase Gradle plugin must be in the buildscript classpath for some strange reason
        // https://github.com/liquibase/liquibase-gradle-plugin/issues/158
        classpath("org.liquibase:liquibase-gradle-plugin:3.0.1")
        classpath("org.liquibase:liquibase-core:4.30.0")
    }
}

plugins {
    id("co.uzzu.dotenv.gradle") version "4.0.+"
    id("org.sonarqube") version "6.+"
    idea
}

allprojects {
    group = "dev.cryptospace"
    version = "1.0.0"
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}
