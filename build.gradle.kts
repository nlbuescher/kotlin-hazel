import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import java.io.ByteArrayOutputStream

buildscript {
    val kotlinVersion by extra("1.3.21")

    repositories {
        jcenter()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

plugins {
    kotlin("multiplatform") version "1.3.21"
}

subprojects {
    group = "hazel"

    val stdout = ByteArrayOutputStream()
    exec {
        commandLine("git", "describe", "--tags")
        standardOutput = stdout
    }

    version = stdout.toString("UTF-8").trim()

    repositories {
        maven(url = "https://dl.bintray.com/dominaezzz/kotlin-native")
        mavenCentral()
        jcenter()
        mavenLocal()
    }

    extra["kotlinxIOVersion"] = "0.1.4"

    afterEvaluate {
        configure<KotlinMultiplatformExtension> {
            sourceSets.all {
                languageSettings.apply {
                    enableLanguageFeature("InlineClasses")
                    useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
                }
            }
        }
    }
}