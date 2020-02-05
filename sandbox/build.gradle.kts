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
				resources.srcDir("src/nativeMain/resources")

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
