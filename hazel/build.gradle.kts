@file:Suppress("UNUSED_VARIABLE")

import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
}

repositories {
    maven("https://dl.bintray.com/dominaezzz/kotlin-native")
    jcenter()
}

val imguiVersion = "0.1.0-dev-3"
val kglVersion = "0.1.9-dev-6"

val os: OperatingSystem = OperatingSystem.current()

kotlin {
    when {
        os.isLinux -> linuxX64("linux")
        os.isWindows -> mingwX64("mingw")
    }

    targets.withType<KotlinNativeTarget> {
        compilations["main"].apply {
            cinterops.create("cstb_image")

            defaultSourceSet {
                kotlin.srcDir("src/nativeMain/kotlin")

                dependencies {
                    implementation("com.kotlin-imgui:imgui:$imguiVersion")
                    implementation("com.kotlin-imgui:imgui-glfw:$imguiVersion")
                    implementation("com.kotlin-imgui:imgui-opengl:$imguiVersion")

                    implementation("com.kgl:kgl-glfw:$kglVersion")
                    implementation("com.kgl:kgl-glfw-static:$kglVersion")
                    implementation("com.kgl:kgl-opengl:$kglVersion")
                }
            }
        }
    }

    sourceSets.all {
        languageSettings.apply {
            enableLanguageFeature("MultiPlatformProjects")
            enableLanguageFeature("InlineClasses")
            useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
            useExperimentalAnnotation("kotlin.time.ExperimentalTime")
            useExperimentalAnnotation("kotlinx.io.core.ExperimentalIoApi")
        }
    }
}
