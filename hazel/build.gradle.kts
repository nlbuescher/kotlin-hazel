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
        val main by compilations.getting {
            cinterops {
                create("cglfw")
                create("cimgui")
                create("copengl")
                create("cstb_image")
            }
            defaultSourceSet {
                kotlin.srcDir("src/nativeMain/kotlin")
            }
        }

        afterEvaluate {
            main.cinterops["cimgui"].apply {
                tasks[interopProcessingTaskName].dependsOn(":cimgui:assembleRelease")
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
        }
    }
}
