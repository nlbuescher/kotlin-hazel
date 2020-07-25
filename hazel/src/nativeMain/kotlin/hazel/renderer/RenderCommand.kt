package hazel.renderer

import hazel.math.*

object RenderCommand {
	private var renderAPI: RenderAPI = RenderAPI()

	fun init() = renderAPI.init()

	fun setViewport(x: Int, y: Int, width: Int, height: Int) {
		renderAPI.setViewport(x, y, width, height)
	}

	fun setClearColor(color: Vec4) {
		renderAPI.setClearColor(color)
	}

	fun clear() {
		renderAPI.clear()
	}

	fun drawIndexed(vertexArray: VertexArray, indexCount: Int = 0) {
		renderAPI.drawIndexed(vertexArray, indexCount)
	}
}
