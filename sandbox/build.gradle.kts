@file:Suppress("UNUSED_VARIABLE")

plugins {
    kotlin("multiplatform")
}

repositories {
    jcenter()
}

kotlin {
    val os = org.gradle.internal.os.OperatingSystem.current()

    if (os.isLinux) linuxX64("linux") {
        binaries {
            executable("Sandbox") {
                entryPoint = "main"
            }
        }
        val main by compilations.existing {
            defaultSourceSet {
                kotlin.srcDir("src/nativeMain/kotlin")
                resources.srcDir("src/nativeMain/resources")

                dependencies {
                    api(project(":hazel"))
                }
            }
        }
    }
}
