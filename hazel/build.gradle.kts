@file:Suppress("UNUSED_VARIABLE")

import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
}

repositories {
    jcenter()
}

val os: OperatingSystem = OperatingSystem.current()

kotlin {
    when {
        os.isLinux -> linuxX64("linux")
        os.isWindows -> mingwX64("mingw")
    }

    targets.withType<KotlinNativeTarget> {
        compilations["main"].apply {
            cinterops {
                create("cglfw")
                create("copengl")
                create("cstb_image")
                create("cimgui") {
                    tasks.named(interopProcessingTaskName) {
                        dependsOn(":cimgui:assembleRelease${os.name}")
                    }
                }
            }
            defaultSourceSet {
                kotlin.srcDir("src/nativeMain/kotlin")
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
