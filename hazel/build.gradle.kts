import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.plugin.mpp.*

plugins {
	kotlin("multiplatform")
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
		compilations {
			all {
				kotlinOptions {
					freeCompilerArgs = listOf("-memory-model", "relaxed")
				}
			}

			named("main") {
				cinterops.create("hazel") {
					includeDirs("src/nativeInterop/include")
				}
			}
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
					listOf("", "-glfw", "-opengl").forEach {
						implementation("com.kotlin-imgui:imgui$it:$imGuiVersion")
					}
					listOf("-glfw", "-glfw-static", "-opengl").forEach {
						implementation("com.kgl:kgl$it:$kglVersion")
					}
				}
			}
		}
	}
}
