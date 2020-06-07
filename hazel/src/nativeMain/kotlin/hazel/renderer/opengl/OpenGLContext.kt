package hazel.renderer.opengl

import com.kgl.glfw.*
import com.kgl.opengl.*
import hazel.core.*
import hazel.core.Window
import hazel.renderer.*

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
