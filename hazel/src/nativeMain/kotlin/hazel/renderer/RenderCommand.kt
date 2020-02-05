package hazel.renderer

import hazel.math.FloatVector4
import hazel.renderer.opengl.OpenGLRenderAPI

private var renderAPI: RenderAPI = OpenGLRenderAPI()

object RenderCommand {
	var api: RenderAPI.API
		get() = RenderAPI.api
		set(new) = run { RenderAPI.api = new }

	fun init() = renderAPI.init()

	fun setViewport(x: Int, y: Int, width: Int, height: Int) {
		renderAPI.setViewport(x, y, width, height)
	}

	fun setClearColor(color: FloatVector4) {
		renderAPI.setClearColor(color)
	}

	fun clear() {
		renderAPI.clear()
	}

	fun drawIndexed(vertexArray: VertexArray) {
		renderAPI.drawIndexed(vertexArray)
	}
}
