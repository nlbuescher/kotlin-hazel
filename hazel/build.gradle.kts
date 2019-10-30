@file:Suppress("UNUSED_VARIABLE")

import org.gradle.internal.os.OperatingSystem

plugins {
    kotlin("multiplatform")
}

repositories {
    jcenter()
}

val kotlinxIoVersion by extra("0.1.15")

kotlin {
    val os = OperatingSystem.current()

    if (os.isLinux) linuxX64("linux") {
        val main by compilations.existing {
            cinterops.create("cglfw")
            cinterops.create("cimgui")
            cinterops.create("copengl")
            cinterops.create("cstb_image")
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
            useExperimentalAnnotation("kotlin.contracts.ExperimentalContracts")
            useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
            useExperimentalAnnotation("kotlin.time.ExperimentalTime")
            useExperimentalAnnotation("kotlinx.io.core.ExperimentalIoApi")
        }
    }
}

tasks.findByName("cinteropCimguiLinux")?.apply {
    dependsOn(":cimgui:build")
}
