import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.plugin.mpp.*

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
		os.isMacOsX -> macosX64("macos")
		os.isWindows -> mingwX64("mingw")
	}

	targets.withType<KotlinNativeTarget> {
		compilations {
			named("main") {
				cinterops.create("stb") {
					includeDirs("src/nativeInterop/include")
				}
			}
		}
	}

	sourceSets {
		targets.withType<KotlinNativeTarget> {
			named("${name}Main") {
				kotlin.srcDir("src/nativeMain/kotlin")
				resources.srcDir("src/nativeMain/resources")
			}

			named("${name}Test") {
				kotlin.srcDir("src/nativeTest/kotlin")
				resources.srcDir("src/nativeTest/resources")
			}
		}
	}
}
