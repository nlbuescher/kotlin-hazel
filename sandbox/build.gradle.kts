@file:Suppress("UNUSED_VARIABLE")

import org.gradle.internal.os.OperatingSystem

plugins {
    kotlin("multiplatform")
}

repositories {
    jcenter()
}

kotlin {
    val os = OperatingSystem.current()

    if (os.isLinux) linuxX64("linux") {
        binaries {
            executable("Sandbox") {
                entryPoint = "main"
            }
        }
        val main by compilations.existing {
            defaultSourceSet {
                kotlin.srcDir("src/nativeMain/kotlin")

                dependencies {
                    api(project(":hazel"))
                }
            }
        }
    }

    sourceSets.all {
        languageSettings.apply {
            enableLanguageFeature("MultiPlatformProjects")
            enableLanguageFeature("InlineClasses")
            useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
        }
    }
}
