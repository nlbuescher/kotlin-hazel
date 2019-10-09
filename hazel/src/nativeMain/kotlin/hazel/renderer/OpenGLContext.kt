package hazel.renderer

import cglfw.glfwMakeContextCurrent
import cglfw.glfwSwapBuffers
import cnames.structs.GLFWwindow
import copengl.glewInit
import kotlinx.cinterop.CPointer

class OpenGLContext(
    private val windowHandle: CPointer<GLFWwindow>
) : GraphicsContext {

    override fun init() {
        glfwMakeContextCurrent(windowHandle)
        glewInit()
    }

    override fun swapBuffers() {
        glfwSwapBuffers(windowHandle)
    }
}
