package hazel.renderer

import hazel.math.*
import hazel.renderer.opengl.*

private var _api: RenderAPI.API = RenderAPI.API.OpenGL

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
			get() = _api
			set(new) {
				_api = new
			}
	}
}

@Suppress("FunctionName")
fun RenderAPI(): RenderAPI = when (Renderer.api) {
	RenderAPI.API.None -> TODO("RenderAPI.API.None is currently not supported")
	RenderAPI.API.OpenGL -> OpenGLRenderAPI()
}
