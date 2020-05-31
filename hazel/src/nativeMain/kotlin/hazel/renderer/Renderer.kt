package hazel.renderer

import hazel.core.Hazel
import hazel.core.profile
import hazel.math.Mat4

class SceneData {
	var viewProjectionMatrix = Mat4.IDENTITY
}

private val sceneData = SceneData()

object Renderer {
	var api: RenderAPI.API
		get() = RenderAPI.api
		set(new) = run { RenderAPI.api = new }

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

	private fun endScene() {

	}

	fun scene(camera: OrthographicCamera, block: Renderer.() -> Unit) {
		beginScene(camera)
		this.block()
		endScene()
	}

	fun submit(shader: Shader, vertexArray: VertexArray, transform: Mat4 = Mat4.IDENTITY) {
		shader.bind()
		shader["u_ViewProjection"] = sceneData.viewProjectionMatrix
		shader["u_Transform"] = transform

		vertexArray.bind()
		RenderCommand.drawIndexed(vertexArray)
	}
}
