package hazel.renderer

import hazel.math.*
import hazel.renderer.opengl.*

private var api: RenderAPI.API = RenderAPI.API.OpenGL

interface RenderAPI {
	enum class API {
		None, OpenGL
	}

	fun init()

	fun setViewport(x: Int, y: Int, width: Int, height: Int)
	fun setClearColor(color: Vec4)
	fun clear()

	fun drawIndexed(vertexArray: VertexArray, indexCount: Int = 0)

	companion object {
		var api: API
			get() = hazel.renderer.api
			set(new) {
				hazel.renderer.api = new
			}
	}
}

@Suppress("FunctionName")
fun RenderAPI(): RenderAPI = when (Renderer.api) {
	RenderAPI.API.None -> TODO("RenderAPI.API.None is currently not supported")
	RenderAPI.API.OpenGL -> OpenGLRenderAPI()
}
