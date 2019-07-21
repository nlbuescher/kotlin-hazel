import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import java.io.ByteArrayOutputStream

plugins {
    kotlin("multiplatform") version "1.3.31" apply false
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
    }

    extra["kotlinxIOVersion"] = "0.1.4"

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
