@file:Suppress("UNUSED_VARIABLE")

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
					implementation("com.kotlin-imgui:imgui:$imguiVersion")
					implementation("com.kotlin-imgui:imgui-glfw:$imguiVersion")
					implementation("com.kotlin-imgui:imgui-opengl:$imguiVersion")

					implementation("com.kgl:kgl-glfw:$kglVersion")
					implementation("com.kgl:kgl-glfw-static:$kglVersion")
					implementation("com.kgl:kgl-opengl:$kglVersion")
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
			useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
			useExperimentalAnnotation("kotlin.time.ExperimentalTime")
		}
	}
}
