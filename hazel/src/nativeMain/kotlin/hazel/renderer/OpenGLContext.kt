package hazel.renderer

import cglfw.glfwMakeContextCurrent
import cglfw.glfwSwapBuffers
import cnames.structs.GLFWwindow
import copengl.GL_RENDERER
import copengl.GL_VENDOR
import copengl.GL_VERSION
import copengl.glGetString
import copengl.glewInit
import hazel.Hazel
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString

class OpenGLContext(
    private val windowHandle: CPointer<GLFWwindow>
) : GraphicsContext {

    private fun CPointer<UByteVar>.toKString() = reinterpret<ByteVar>().toKString()

    override fun init() {
        glfwMakeContextCurrent(windowHandle)
        glewInit()

        Hazel.coreInfo {
            """
            OpenGL Info:
              Vendor: ${glGetString(GL_VENDOR)?.toKString()}
              Renderer: ${glGetString(GL_RENDERER)?.toKString()}
              Version: ${glGetString(GL_VERSION)?.toKString()}
            """.trimIndent()
        }
    }

    override fun swapBuffers() {
        glfwSwapBuffers(windowHandle)
    }
}
