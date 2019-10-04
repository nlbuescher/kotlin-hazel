@file:Suppress("UNUSED_VARIABLE")

import org.jetbrains.kotlin.gradle.tasks.CInteropProcess

plugins {
    kotlin("multiplatform")
}

repositories {
    jcenter()
}

val kotlinxIoVersion by extra("0.1.15")

kotlin {
    val os = org.gradle.internal.os.OperatingSystem.current()

    if (os.isLinux) linuxX64("linux") {
        val main by compilations.existing {
            cinterops.create("cglfw")
            cinterops.create("cimgui")
            cinterops.create("copengl")
            defaultSourceSet {
                kotlin.srcDir("src/nativeMain/kotlin")
                resources.srcDir("src/nativeMain/resources")

                dependencies {
                    implementation("org.jetbrains.kotlinx:kotlinx-io-native:$kotlinxIoVersion")
                }
            }
        }
        val test by compilations.existing {
            defaultSourceSet {
                kotlin.srcDir("src/nativeTest/kotlin")
                resources.srcDir("src/nativeTest/resources")
            }
        }
    }

    sourceSets.all {
        languageSettings.apply {
            enableLanguageFeature("MultiPlatformProjects")
            enableLanguageFeature("InlineClasses")
            useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
            useExperimentalAnnotation("kotlin.time.ExperimentalTime")
        }
    }
}

tasks.findByName("cinteropCimguiLinux")?.apply {
    dependsOn(":cimgui:build")
}
