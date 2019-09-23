@file:Suppress("UNUSED_VARIABLE")

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    kotlin("multiplatform") version "1.3.50" apply false
}

subprojects {
    group = "hazel"
    version = "1.0.0-SNAPSHOT"

    repositories {
        maven(url = "https://dl.bintray.com/dominaezzz/kotlin-native")
        mavenCentral()
        jcenter()
    }

    val kotlinxIoVersion by extra("0.1.14")

    afterEvaluate {
        configure<KotlinMultiplatformExtension> {
            sourceSets.all {
                languageSettings.apply {
                    enableLanguageFeature("MultiPlatformProjects")
                    enableLanguageFeature("InlineClasses")
                    useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
                }
            }
        }
    }
}
