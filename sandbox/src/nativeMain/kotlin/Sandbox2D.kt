import com.imgui.ImGui
import hazel.core.*
import hazel.math.*
import hazel.renderer.OrthographicCameraController
import hazel.renderer.RenderCommand
import hazel.renderer.Renderer2D
import hazel.renderer.Texture2D

class Sandbox2D : Layer("Sandbox2D") {
	private val cameraController = OrthographicCameraController(1280f / 720f, true)

	private val squareColor = MutableVec4(0f, 0f, 1f, 1f)

	private lateinit var checkerBoardTexture: Texture2D


	override fun onAttach() {
		checkerBoardTexture = Texture2D("assets/textures/checkerboard.png")
	}

	override fun onDetach() {}

	override fun onUpdate(timeStep: TimeStep) {
		Hazel.profile(::onUpdate) {
			// update
			cameraController.onUpdate(timeStep)

			// render
			Hazel.profile("Renderer Prep") {
				RenderCommand.setClearColor(Vec4(0.1f, 0.1f, 0.1f, 1f))
				RenderCommand.clear()
			}

			Hazel.profile("Renderer Draw") {
				Renderer2D.scene(cameraController.camera) {
					drawRotatedQuad(Vec2(-1f, 0f), Vec2(0.8f, 0.8f), 45f.degrees, Vec4(1f, 0f, 0f, 1f))
					drawQuad(Vec2(0.5f, -0.5f), Vec2(0.5f, 0.75f), Vec4(0f, 1f, 0f, 1f))
					drawQuad(Vec3(0f, 0f, -0.1f), Vec2(10f, 10f), checkerBoardTexture, 10f, Vec4(1f, 0.9f, 0.9f, 1f))
				}
			}
		}
	}

	override fun onImGuiRender() {
		with(ImGui) {
			begin("Settings")
			colorEdit4("Square Color", squareColor.asFloatArray())
			end()
		}
	}

	override fun onEvent(event: Event) {
		cameraController.onEvent(event)
	}
}
