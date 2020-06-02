package hazel.renderer.opengl

import com.kgl.glfw.Glfw
import com.kgl.opengl.GL_RENDERER
import com.kgl.opengl.GL_VENDOR
import com.kgl.opengl.GL_VERSION
import com.kgl.opengl.glGetString
import hazel.core.Hazel
import hazel.core.Window
import hazel.core.coreInfo
import hazel.core.profile
import hazel.renderer.GraphicsContext

internal class OpenGLContext(private val window: Window) : GraphicsContext {
	override fun init() {
		Hazel.profile("OpenGLContext.init()") {
			Glfw.currentContext = window.nativeWindow

			Hazel.coreInfo("OpenGL Info:")
			Hazel.coreInfo("  Vendor: ${glGetString(GL_VENDOR)}")
			Hazel.coreInfo("  Renderer: ${glGetString(GL_RENDERER)}")
			Hazel.coreInfo("  Version: ${glGetString(GL_VERSION)}")
		}
	}

	override fun swapBuffers() {
		Hazel.profile("OpenGLContext.swapBuffers()") {
			window.nativeWindow.swapBuffers()
		}
	}
}
