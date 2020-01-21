@file:Suppress("UNUSED_VARIABLE")

import org.gradle.internal.os.OperatingSystem

plugins {
    kotlin("multiplatform")
}

repositories {
    maven("https://dl.bintray.com/dominaezzz/kotlin-native")
    jcenter()
}

val kglVersion = "0.1.9-dev-5"

kotlin {
    val os = OperatingSystem.current()
    when {
        os.isLinux -> linuxX64("linux") {
            val main by compilations.getting {
                cinterops.create("cstb_image")

                defaultSourceSet {
                    kotlin.srcDir("src/nativeMain/kotlin")

                    dependencies {
                        api(project(":imgui"))
                        implementation("com.kgl:kgl-glfw:$kglVersion")
                        implementation("com.kgl:kgl-glfw-static:$kglVersion")
                        implementation("com.kgl:kgl-opengl:$kglVersion")
                    }
                }
            }
        }
        os.isWindows -> mingwX64("mingw") {
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
                    tasks[interopProcessingTaskName].dependsOn(":cimgui:assembleReleaseWindows")
                }
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
