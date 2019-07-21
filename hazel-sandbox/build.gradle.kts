plugins {
    kotlin("multiplatform")
}

kotlin {
    val os = org.gradle.internal.os.OperatingSystem.current()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }

    if (os.isLinux) linuxX64("linux") {
        binaries {
            executable()
        }
        val main by compilations.getting {
            defaultSourceSet {
                kotlin.srcDir("src/nativeMain/kotlin")
                resources.srcDir("src/nativeMain/resources")

                dependencies {
                    api(project(":hazel-engine"))
                }
            }
        }
    }
    if (os.isMacOsX) macosX64("macos") {
        binaries {
            executable()
        }
        val main by compilations.getting {
            defaultSourceSet {
                kotlin.srcDir("src/nativeMain/kotlin")
                resources.srcDir("src/nativeMain/resources")

                dependencies {
                    api(project(":hazel-engine"))
                }
            }
        }
    }
    if (os.isWindows) mingwX64("mingw") {
        binaries {
            executable()
        }
        val main by compilations.getting {
            defaultSourceSet {
                kotlin.srcDir("src/nativeMain/kotlin")
                resources.srcDir("src/nativeMain/resources")

                dependencies {
                    api(project(":hazel-engine"))
                }
            }
        }
    }
}
