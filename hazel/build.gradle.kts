@file:Suppress("UNUSED_VARIABLE")

import de.undercouch.gradle.tasks.download.Download
import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
	kotlin("multiplatform")
	id("de.undercouch.download")
}

repositories {
	maven("https://dl.bintray.com/dominaezzz/kotlin-native")
	jcenter()
}

val os: OperatingSystem = OperatingSystem.current()

val downloadsDir = buildDir.resolve("downloads")
val stbDir = downloadsDir.resolve("stb-master")

val downloadArchive by tasks.registering(Download::class) {
	src("https://github.com/nothings/stb/archive/master.zip")
	dest(downloadsDir.resolve("master.zip"))
	overwrite(false)
}

val unzipArchive by tasks.registering(Copy::class) {
	dependsOn(downloadArchive)

	from(downloadArchive.map { zipTree(it.dest) })
	into(downloadsDir)
}

val konanUserDir = file(System.getenv("KONAN_DATA_DIR") ?: "${System.getProperty("user.home")}/.konan")
val toolChainFolderName = when {
	os.isLinux -> "clang-llvm-8.0.0-linux-x86-64"
	os.isMacOsX -> "clang-llvm-apple-8.0.0-darwin-macos"
	os.isWindows -> "msys2-mingw-w64-x86_64-clang-llvm-lld-compiler_rt-8.0.1"
	else -> TODO()
}
val llvmBinFolder = konanUserDir.resolve("dependencies/${toolChainFolderName}/bin")

val cppFile = file("src/nativeInterop/cinterop/stb.cpp")
val objFile = buildDir.resolve("lib/stb.o")
val libFile = buildDir.resolve("lib/libstb.a")

val compileStb by tasks.registering(Exec::class) {
	dependsOn(unzipArchive)
	commandLine(
		llvmBinFolder.resolve("clang++").absolutePath,
		"-c", "-w",
		"-o", objFile.absolutePath,
		"-I", stbDir.absolutePath,
		cppFile.absolutePath
	)
	environment(
		"PATH" to "$llvmBinFolder;${System.getenv("PATH")}",
		"CPATH" to "/Applications/Xcode.app/Contents/Developer/Platforms/MaxOSX.platform/Developer/SDKs/MacOSX.sdk/usr/include"
	)
	inputs.file(cppFile)
	outputs.file(objFile)
}

val archiveStb by tasks.registering(Exec::class) {
	dependsOn(compileStb)

	commandLine(
		llvmBinFolder.resolve("llvm-ar").absolutePath,
		"rc", libFile.absolutePath,
		objFile.absolutePath
	)
	environment("PATH", "$llvmBinFolder;${System.getenv("PATH")}")
	inputs.file(objFile)
	outputs.file(libFile)
}

val imguiVersion = "0.1.0-dev-3"
val kglVersion = "0.1.9-dev-6"

kotlin {
	when {
		os.isLinux -> linuxX64("linux")
		os.isWindows -> mingwX64("mingw")
	}

	targets.withType<KotlinNativeTarget> {
		compilations["main"].apply {
			cinterops.create("cstb") {
				tasks[interopProcessingTaskName].dependsOn(unzipArchive)
				includeDirs(stbDir)
			}

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

			compileKotlinTaskHolder.configure {
				dependsOn(archiveStb)
				kotlinOptions {
					freeCompilerArgs = listOf("-include-binary", libFile.absolutePath)
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
			useExperimentalAnnotation("kotlinx.io.core.ExperimentalIoApi")
		}
	}
}
