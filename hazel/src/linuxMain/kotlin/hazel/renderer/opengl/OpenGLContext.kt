package hazel.renderer.opengl

import com.kgl.glfw.Glfw
import com.kgl.opengl.GL_RENDERER
import com.kgl.opengl.GL_VENDOR
import com.kgl.opengl.GL_VERSION
import com.kgl.opengl.glGetString
import hazel.core.Hazel
import hazel.core.Window
import hazel.renderer.GraphicsContext
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString

internal class OpenGLContext(private val window: Window) : GraphicsContext {

    private fun CPointer<UByteVar>.toKString() = reinterpret<ByteVar>().toKString()

    override fun init() {
        Glfw.currentContext = window.internal

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
        window.internal.swapBuffers()
    }
}
