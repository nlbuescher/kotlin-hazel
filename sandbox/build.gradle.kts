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
        binaries {
            executable("Sandbox") {
                entryPoint = "main"
            }
        }
        compilations["main"].apply {
            defaultSourceSet {
                kotlin.srcDir("src/nativeMain/kotlin")

                dependencies {
                    api(project(":hazel"))
                }
            }
        }
    }

    sourceSets.all {
        languageSettings.apply {
            enableLanguageFeature("MultiPlatformProjects")
            enableLanguageFeature("InlineClasses")
            useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
        }
    }
}
