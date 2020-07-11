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
		compilations["main"].apply {
			defaultSourceSet {
				kotlin.srcDir("src/nativeMain/kotlin")
				resources.srcDir("src/nativeMain/resources")
			}
			dependencies {
				api(project(":hazel"))
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
