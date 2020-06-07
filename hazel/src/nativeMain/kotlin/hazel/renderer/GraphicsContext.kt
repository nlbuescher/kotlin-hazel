package hazel.renderer

import hazel.core.*
import hazel.renderer.opengl.*

interface GraphicsContext {
	fun init()
	fun swapBuffers()
}

@Suppress("FunctionName")
fun GraphicsContext(window: Window): GraphicsContext = when (Renderer.api) {
	RenderAPI.API.None -> TODO("RenderAPI.API.None is currently not supported")
	RenderAPI.API.OpenGL -> OpenGLContext(window)
}
