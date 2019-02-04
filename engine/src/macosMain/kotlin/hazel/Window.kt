package hazel

import cnames.structs.GLFWwindow
import glfw.*
import kotlinx.cinterop.CPointer
import platform.OpenGL3.GL_COLOR_BUFFER_BIT
import platform.OpenGL3.glClear
import platform.OpenGL3.glClearColor


var isGlfwInitialized = false


@ExperimentalUnsignedTypes
class MacosWindow(
    title: String,
    width: UInt,
    height: UInt
) : Window(title, width, height) {
    private val window: CPointer<GLFWwindow>

    override var enableVSync: Boolean = false
        set(value) {
            field = value
            if (value) glfwSwapInterval(1)
            else glfwSwapInterval(0)
        }

    override val shouldClose: Boolean get() = glfwWindowShouldClose(window) > 0

    init {
        Hazel.coreInfo("Creating window $title ($width, $height)")

        if (!isGlfwInitialized) {
            val success = glfwInit() > 0
            Hazel.coreAssert(success, "Could not initialize GLFW!")
            isGlfwInitialized = true
        }

        window = glfwCreateWindow(width.toInt(), height.toInt(), title, null, null) ?: error("Could not create window!")
        glfwMakeContextCurrent(window)
        enableVSync = true
    }

    override fun onUpdate() {
        glfwPollEvents()
    }

    override fun onDestroy() {
        glfwDestroyWindow(window)
    }
}


@ExperimentalUnsignedTypes
actual fun createWindow(title: String, width: UInt, height: UInt): Window = MacosWindow(title, width, height)
