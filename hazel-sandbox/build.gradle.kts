@file:Suppress("UNUSED_VARIABLE")

plugins {
    kotlin("multiplatform")
}

kotlin {
    val os = org.gradle.internal.os.OperatingSystem.current()

    if (os.isLinux) linuxX64("linux") {
        binaries {
            executable()
        }
        val main by compilations.existing {
            defaultSourceSet {
                kotlin.srcDir("src/nativeMain/kotlin")
                resources.srcDir("src/nativeMain/resources")

                dependencies {
                    api(project(":hazel-engine"))
                }
            }
        }
    }
    if (os.isMacOsX) macosX64("macos") {
        binaries {
            executable()
        }
        val main by compilations.existing {
            defaultSourceSet {
                kotlin.srcDir("src/nativeMain/kotlin")
                resources.srcDir("src/nativeMain/resources")

                dependencies {
                    api(project(":hazel-engine"))
                }
            }
        }
    }
    if (os.isWindows) mingwX64("mingw") {
        binaries {
            executable()
        }
        val main by compilations.existing {
            defaultSourceSet {
                kotlin.srcDir("src/nativeMain/kotlin")
                resources.srcDir("src/nativeMain/resources")

                dependencies {
                    api(project(":hazel-engine"))
                }
            }
        }
    }
}
