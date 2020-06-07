import com.imgui.*
import hazel.core.*
import hazel.events.*
import hazel.math.*
import hazel.renderer.*

class Sandbox2D : Layer("Sandbox2D") {
	private val cameraController = OrthographicCameraController(1280f / 720f, true)

	private val squareColor = MutableVec4(0f, 0f, 1f, 1f)

	private lateinit var checkerBoardTexture: Texture2D


	override fun onAttach() {
		Hazel.profile("Sandbox2D.onAttach()") {
			checkerBoardTexture = Texture2D("assets/textures/checkerboard.png")
		}
	}

	override fun onDetach() {
		Hazel.profile("Sandbox2D.onDetach()") {}
	}

	override fun onUpdate(timeStep: TimeStep) {
		Hazel.profile("Sandbox2D.onUpdate(TimeStep)") {
			// update
			cameraController.onUpdate(timeStep)

			// render
			Hazel.profile("Renderer Prep") {
				RenderCommand.setClearColor(Vec4(0.1f, 0.1f, 0.1f, 1f))
				RenderCommand.clear()
			}

			Hazel.profile("Renderer Draw") {
				Renderer2D.scene(cameraController.camera) {
					//drawRotatedQuad(Vec2(-1f, 0f), Vec2(0.8f, 0.8f), 45f.degrees, Vec4(1f, 0f, 0f, 1f))
					drawQuad(Vec2(-1f, 0f), Vec2(0.8f, 0.8f), Vec4(1f, 0f, 0f, 1f))
					drawQuad(Vec2(0.5f, -0.5f), Vec2(0.5f, 0.75f), Vec4(0f, 1f, 0f, 1f))
					//drawQuad(Vec3(0f, 0f, -0.1f), Vec2(10f, 10f), checkerBoardTexture, 10f)
				}
			}
		}
	}

	override fun onImGuiRender() {
		Hazel.profile("Sandbox2D.onImGuiRender()") {
			with(ImGui) {
				begin("Settings")
				colorEdit4("Square Color", squareColor.asFloatArray())
				end()
			}
		}
	}

	override fun onEvent(event: Event) {
		cameraController.onEvent(event)
	}
}
