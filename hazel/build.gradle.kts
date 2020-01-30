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

val osName: String? = when {
    os.isLinux -> "Linux"
    os.isWindows -> "Windows"
    else -> null
}

val cppTargetName = osName?.toLowerCase()

val cimguiFile: String? = when {
    os.isLinux -> "libcimgui.a"
    os.isWindows -> "cimgui.lib"
    else -> null
}

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
                        dependsOn(":cimgui:assembleRelease$osName")
                    }
                }
            }
            defaultSourceSet {
                kotlin.srcDir("src/nativeMain/kotlin")
            }
            kotlinOptions {
                val binary = file("../cimgui/build/lib/main/release/$cppTargetName/$cimguiFile")
                freeCompilerArgs = listOf("-include-binary", binary.absolutePath)
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
