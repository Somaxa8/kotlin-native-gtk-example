plugins {
    kotlin("multiplatform") version "1.5.10"
}

group = "me.somaxa8"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    val hostOs = System.getProperty("os.name")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
        compilations["main"].cinterops {
            val gtk3 by creating {
                when (preset) {
                    presets["macosX64"], presets["linuxX64"] -> {
                        listOf("/opt/local/include", "/usr/include", "/usr/local/include").forEach {
                            includeDirs(
                                "$it/atk-1.0",
                                "$it/gdk-pixbuf-2.0",
                                "$it/cairo",
                                "$it/harfbuzz",
                                "$it/pango-1.0",
                                "$it/gtk-3.0",
                                "$it/glib-2.0"
                            )
                        }

                        includeDirs(
                            "/opt/local/lib/glib-2.0/include",
                            "/usr/lib/x86_64-linux-gnu/glib-2.0/include",
                            "/usr/local/lib/glib-2.0/include"
                        )
                    }
                }
            }
        }
    }
}
