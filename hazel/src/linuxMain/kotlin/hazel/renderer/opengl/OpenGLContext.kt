package hazel.renderer.opengl

import com.kgl.glfw.Glfw
import com.kgl.opengl.GL_RENDERER
import com.kgl.opengl.GL_VENDOR
import com.kgl.opengl.GL_VERSION
import com.kgl.opengl.glGetString
import hazel.core.Hazel
import hazel.core.Window
import hazel.renderer.GraphicsContext

internal class OpenGLContext(private val window: Window) : GraphicsContext {

    override fun init() {
        Glfw.currentContext = window.internal

        Hazel.coreInfo {
            """
            OpenGL Info:
              Vendor: ${glGetString(GL_VENDOR)}
              Renderer: ${glGetString(GL_RENDERER)}
              Version: ${glGetString(GL_VERSION)}
            """.trimIndent()
        }
    }

    override fun swapBuffers() {
        window.internal.swapBuffers()
    }
}
