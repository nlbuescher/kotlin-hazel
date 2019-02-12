plugins {
    id("maven-publish")
    kotlin("multiplatform")
}

kotlin {
    val os = org.gradle.internal.os.OperatingSystem.current()

    if (os.isLinux) linuxX64("linux") {
        val main by compilations.getting {
            cinterops.create("cglfw")
            defaultSourceSet {
                kotlin.srcDir("src/nativeMain/kotlin")
                resources.srcDir("src/nativeMain/resources")
            }
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-io-native:${extra["kotlinxIOVersion"]}")
            }
        }
        val test by compilations.getting {
            defaultSourceSet {
                kotlin.srcDir("src/nativeTest/kotlin")
                resources.srcDir("src/nativeTest/resources")
            }
        }
    }
    if (os.isMacOsX) macosX64("macos") {
        val main by compilations.getting {
            defaultSourceSet {
                kotlin.srcDir("src/nativeMain/kotlin")
                resources.srcDir("src/nativeMain/resources")
            }
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-io-native:${extra["kotlinxIOVersion"]}")
            }
        }
        val test by compilations.getting {
            defaultSourceSet {
                kotlin.srcDir("src/nativeTest/kotlin")
                resources.srcDir("src/nativeTest/resources")
            }
        }
    }
    if (os.isWindows) mingwX64("mingw") {
        val main by compilations.getting {
            defaultSourceSet {
                kotlin.srcDir("src/nativeMain/kotlin")
                resources.srcDir("src/nativeMain/resources")
            }
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-io-native:${extra["kotlinxIOVersion"]}")
            }
        }
        val test by compilations.getting {
            defaultSourceSet {
                kotlin.srcDir("src/nativeTest/kotlin")
                resources.srcDir("src/nativeTest/resources")
            }
        }
    }
}
