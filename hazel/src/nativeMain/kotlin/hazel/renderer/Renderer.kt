package hazel.renderer

import hazel.core.*
import hazel.math.*

@ThreadLocal
object Renderer {
	private class SceneData {
		var viewProjectionMatrix = Mat4()
	}

	private val sceneData = SceneData()

	var api: RenderAPI.API
		get() = RenderAPI.api
		set(value) {
			RenderAPI.api = value
		}

	fun init() {
		Hazel.profile("Renderer.init()") {
			RenderCommand.init()
			Renderer2D.init()
		}
	}

	fun shutdown() {
		Hazel.profile("Renderer.shutdown()") {
			Renderer2D.shutdown()
		}
	}

	fun onWindowResize(width: Int, height: Int) {
		RenderCommand.setViewport(0, 0, width, height)
	}

	private fun beginScene(camera: OrthographicCamera) {
		sceneData.viewProjectionMatrix = camera.viewProjectionMatrix
	}

	private fun endScene() {}

	fun scene(camera: OrthographicCamera, block: Renderer.() -> Unit) {
		beginScene(camera)
		this.block()
		endScene()
	}

	fun submit(shader: Shader, vertexArray: VertexArray, transform: Mat4 = Mat4()) {
		shader.bind()
		shader["u_ViewProjection"] = sceneData.viewProjectionMatrix
		shader["u_Transform"] = transform

		vertexArray.bind()
		RenderCommand.drawIndexed(vertexArray)
	}
}
