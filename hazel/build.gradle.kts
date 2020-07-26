import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.plugin.mpp.*

plugins {
	kotlin("multiplatform")
}

repositories {
	mavenLocal()
	maven("https://dl.bintray.com/dominaezzz/kotlin-native")
	jcenter()
}

val os: OperatingSystem = OperatingSystem.current()

val imGuiVersion = "docking" //"0.1.2"
val kglVersion = "0.1.9-dev-13"

kotlin {
	when {
		os.isLinux -> linuxX64("linux")
		os.isMacOsX -> macosX64("macos")
		os.isWindows -> mingwX64("mingw")
	}

	targets.withType<KotlinNativeTarget> {
		compilations {
			"main" {
				cinterops.create("hazel")
				kotlinOptions {
					freeCompilerArgs = listOf("-memory-model", "relaxed")
				}
				defaultSourceSet {
					kotlin.srcDir("src/nativeMain/kotlin")
					resources.srcDir("src/nativeMain/resources")
				}
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

	sourceSets {
		all {
			languageSettings.apply {
				enableLanguageFeature("MultiPlatformProjects")
				enableLanguageFeature("InlineClasses")
				enableLanguageFeature("NewInference")
				useExperimentalAnnotation("kotlin.RequiresOptIn")
				useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
				useExperimentalAnnotation("kotlin.time.ExperimentalTime")
			}
		}
	}
}
