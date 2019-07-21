@file:Suppress("UNUSED_VARIABLE")

plugins {
    id("maven-publish")
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
        val main by compilations.getting {
            cinterops.create("cglfw")
            cinterops.create("cimgui")
            defaultSourceSet {
                kotlin.srcDir("src/nativeMain/kotlin")
                resources.srcDir("src/nativeMain/resources")

                dependencies {
                    implementation("org.jetbrains.kotlinx:kotlinx-io-native:${extra["kotlinxIOVersion"]}")
                    implementation("com.kgl:kgl-opengl:0.1.5")
                }
            }
        }
        val test by compilations.getting {
            defaultSourceSet {
                kotlin.srcDir("src/nativeTest/kotlin")
                resources.srcDir("src/nativeTest/resources")
            }
        }
    }
    if (os.isMacOsX) macosX64("macos") {
        val main by compilations.getting {
            cinterops.create("cglfw")
            defaultSourceSet {
                kotlin.srcDir("src/nativeMain/kotlin")
                resources.srcDir("src/nativeMain/resources")

                dependencies {
                    implementation("org.jetbrains.kotlinx:kotlinx-io-native:${extra["kotlinxIOVersion"]}")
                }
            }
        }
        val test by compilations.getting {
            defaultSourceSet {
                kotlin.srcDir("src/nativeTest/kotlin")
                resources.srcDir("src/nativeTest/resources")
            }
        }
    }
    if (os.isWindows) mingwX64("mingw") {
        val main by compilations.getting {
            defaultSourceSet {
                kotlin.srcDir("src/nativeMain/kotlin")
                resources.srcDir("src/nativeMain/resources")

                dependencies {
                    implementation("org.jetbrains.kotlinx:kotlinx-io-native:${extra["kotlinxIOVersion"]}")
                }
            }
        }
        val test by compilations.getting {
            defaultSourceSet {
                kotlin.srcDir("src/nativeTest/kotlin")
                resources.srcDir("src/nativeTest/resources")
            }
        }
    }
}
