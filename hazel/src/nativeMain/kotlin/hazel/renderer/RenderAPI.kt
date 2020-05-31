package hazel.renderer

import hazel.math.Vec4

private var _api: RenderAPI.API = RenderAPI.API.OpenGL

interface RenderAPI {

	enum class API {
		None, OpenGL
	}

	fun init()

	fun setViewport(x: Int, y: Int, width: Int, height: Int)
	fun setClearColor(color: Vec4)
	fun clear()

	fun drawIndexed(vertexArray: VertexArray)


	companion object {
		var api: API
			get() = _api
			set(new) = run { _api = new }
	}
}
