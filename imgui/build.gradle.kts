import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
}

repositories {
    jcenter()
}

val osName: String? = OperatingSystem.current().let { os ->
    when {
        os.isLinux -> "Linux"
        os.isMacOsX -> "MacOsX"
        os.isWindows -> "Windows"
        else -> ""
    }
}

kotlin {
    val os = OperatingSystem.current()
    when {
        os.isLinux -> linuxX64("linux")
        os.isMacOsX -> macosX64("macos")
        os.isWindows -> mingwX64("mingw")
    }

    targets.withType<KotlinNativeTarget> {
        compilations["main"].apply {
            cinterops.create("cimgui") {
                tasks.named(interopProcessingTaskName) {
                    dependsOn("cimgui:assembleRelease$osName")
                }
            }
            defaultSourceSet {
                kotlin.srcDir("src/nativeMain/kotlin")
            }
            val binary = file("cimgui/build/lib/main/release/${osName?.toLowerCase()}/libcimgui.a")
            kotlinOptions {
                println(binary.absolutePath)
                freeCompilerArgs = listOf("-ib", binary.absolutePath)
            }
        }
    }
}
