package hazel

import cnames.structs.GLFWwindow
import glfw.*
import kotlinx.cinterop.CPointer


var isGlfwInitialized = false


@ExperimentalUnsignedTypes
class LinuxWindow(
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
actual fun createWindow(title: String, width: UInt, height: UInt): Window = LinuxWindow(title, width, height)