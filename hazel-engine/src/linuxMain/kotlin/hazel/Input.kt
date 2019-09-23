package hazel

import cglfw.GLFW_PRESS
import cglfw.GLFW_REPEAT
import cglfw.glfwGetCursorPos
import cglfw.glfwGetKey
import cglfw.glfwGetMouseButton
import kotlinx.cinterop.DoubleVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value

actual object Input {
    actual fun isKeyPressed(keycode: Int): Boolean {
        val state = glfwGetKey(Hazel.application.window.ptr, keycode)
        return state == GLFW_PRESS || state == GLFW_REPEAT
    }

    actual fun isMouseButtonPressed(button: Int): Boolean {
        val state = glfwGetMouseButton(Hazel.application.window.ptr, button)
        return state == GLFW_PRESS
    }

    actual val mousePosition: Pair<Float, Float>
        get() = memScoped {
            val x = alloc<DoubleVar>()
            val y = alloc<DoubleVar>()
            glfwGetCursorPos(Hazel.application.window.ptr, x.ptr, y.ptr)
            x.value.toFloat() to y.value.toFloat()
        }
}
