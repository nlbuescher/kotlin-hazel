group = "hazel"
version = "1.0-SNAPSHOT"

plugins {
    id("maven-publish")
    kotlin("multiplatform") version "1.3.20"
}

kotlin {
    macosX64("macos") {
        binaries {
            sharedLib()
        }
    }
    linuxX64("linux") {
        binaries {
            sharedLib()
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(kotlin("stdlib"))
            }
        }
    }
}
