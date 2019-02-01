group = "hazel"
version = "1.0-SNAPSHOT"

plugins {
    id("maven-publish")
    kotlin("multiplatform") version "1.3.20"
}

kotlin {
    linuxX64("linux") {
        binaries {
            sharedLib()
        }
    }
    sourceSets {
        val linuxMain by getting {}
        val linuxTest by getting {}
    }
}
