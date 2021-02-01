import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.plugin.mpp.*

plugins {
	kotlin("multiplatform")
	kotlin("plugin.serialization")
}

repositories {
	maven("https://dl.bintray.com/dominaezzz/kotlin-native")
	jcenter()
}

val os: OperatingSystem = OperatingSystem.current()

val imGuiVersion = "0.1.7-docking"
val kglVersion = "0.1.10"

kotlin {
	when {
		os.isLinux -> linuxX64("linux")
		os.isMacOsX -> macosX64("macos")
		os.isWindows -> mingwX64("mingw")
	}

	targets.withType<KotlinNativeTarget> {
		compilations.named("main") {
			cinterops.create("hazel")
		}
	}

	sourceSets {
		all {
			languageSettings.apply {
				enableLanguageFeature("InlineClasses")
				useExperimentalAnnotation("kotlin.RequiresOptIn")
				useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
				useExperimentalAnnotation("kotlin.time.ExperimentalTime")
			}
		}

		targets.withType<KotlinNativeTarget> {
			named("${name}Main") {
				kotlin.srcDir("src/nativeMain/kotlin")
				resources.srcDir("src/nativeMain/resources")

				dependencies {
					implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.1")
					implementation("net.mamoe.yamlkt:yamlkt:0.7.4")

					listOf("", "-glfw", "-opengl").forEach {
						implementation("com.kotlin-imgui:imgui$it:$imGuiVersion")
					}
					listOf("-glfw", "-glfw-static", "-opengl", "-stb").forEach {
						implementation("com.kgl:kgl$it:$kglVersion")
					}
				}
			}

			named("${name}Test") {
				kotlin.srcDir("src/nativeTest/kotlin")
				resources.srcDir("src/nativeTest/resources")
			}
		}
	}
}
