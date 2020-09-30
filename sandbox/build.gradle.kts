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

kotlin {
	when {
		os.isLinux -> linuxX64("linux")
		os.isMacOsX -> macosX64("macos")
		os.isWindows -> mingwX64("mingw")
	}

	targets.withType<KotlinNativeTarget> {
		binaries {
			executable("Sandbox") {
				entryPoint = "main"
			}
		}

		compilations.all {
			kotlinOptions {
				freeCompilerArgs = listOf("-memory-model", "relaxed")
			}
		}
	}

	sourceSets {
		all {
			languageSettings.apply {
				enableLanguageFeature("InlineClasses")
				useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
			}
		}

		targets.withType<KotlinNativeTarget> {
			named("${name}Main") {
				kotlin.srcDir("src/nativeMain/kotlin")
				resources.srcDir("src/nativeMain/resources")

				dependencies {
					implementation(project(":hazel"))
				}
			}
		}
	}
}
