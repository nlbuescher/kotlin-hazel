import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
	kotlin("multiplatform")
}

repositories {
	maven("https://dl.bintray.com/dominaezzz/kotlin-native")
	jcenter()
}

val os: OperatingSystem = OperatingSystem.current()

val imguiVersion = "0.1.1"
val kglVersion = "0.1.9-dev-8"

kotlin {
	when {
		os.isLinux -> linuxX64("linux")
		os.isMacOsX -> macosX64("macos")
		os.isWindows -> mingwX64("mingw")
	}

	targets.withType<KotlinNativeTarget> {
		compilations["main"].apply {
			cinterops.create("hazel")

			defaultSourceSet {
				kotlin.srcDir("src/nativeMain/kotlin")
				resources.srcDir("src/nativeMain/resources")

				dependencies {
					listOf("", "-glfw", "-opengl").forEach {
						implementation("com.kotlin-imgui:imgui$it:$imguiVersion")
					}
					listOf("-glfw", "-glfw-static", "-opengl").forEach {
						implementation("com.kgl:kgl$it:$kglVersion")
					}
				}
			}
		}
	}

	sourceSets {
		commonMain {
			dependencies {
				implementation(kotlin("stdlib-common"))
			}
		}
	}

	sourceSets.all {
		languageSettings.apply {
			enableLanguageFeature("MultiPlatformProjects")
			enableLanguageFeature("InlineClasses")
			//useExperimentalAnnotation("kotlin.RequiresOptIn")
			useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
			useExperimentalAnnotation("kotlin.time.ExperimentalTime")
		}
	}
}
