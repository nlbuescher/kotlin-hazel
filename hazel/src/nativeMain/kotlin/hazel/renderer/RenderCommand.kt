package hazel.renderer

import hazel.math.Vec4

private var renderAPI: RenderAPI = RenderAPI()

object RenderCommand {
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

	fun drawIndexed(vertexArray: VertexArray) {
		renderAPI.drawIndexed(vertexArray)
	}
}
