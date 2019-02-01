group = "hazel"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("multiplatform") version "1.3.20"
}

kotlin {
    linuxX64("linux") {
        binaries {
            executable()
        }
    }
    sourceSets {
        val linuxMain by getting {
            dependencies {
                api(project(":engine"))
            }
        }
        val linuxTest by getting {}
    }
}
