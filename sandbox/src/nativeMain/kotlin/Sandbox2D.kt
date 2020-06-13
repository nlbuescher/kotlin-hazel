import com.imgui.*
import hazel.core.*
import hazel.events.*
import hazel.math.*
import hazel.renderer.*

class Sandbox2D : Layer("Sandbox2D") {
	private val cameraController = OrthographicCameraController(1280f / 720f, true)

	private lateinit var checkerBoardTexture: Texture2D


	override fun onAttach() {
		Hazel.profile("Sandbox2D.onAttach()") {
			checkerBoardTexture = Texture2D("assets/textures/checkerboard.png")
		}
	}

	override fun onDetach() {
		Hazel.profile("Sandbox2D.onDetach()") {}
	}

	private var rotation = 0f
	private val squareColor = MutableVec4(0.2f, 0.3f, 0.8f, 1f)

	override fun onUpdate(timeStep: TimeStep) {
		Hazel.profile("Sandbox2D.onUpdate(TimeStep)") {
			// update
			cameraController.onUpdate(timeStep)

			// render
			Renderer2D.resetStats()
			Hazel.profile("Renderer Prep") {
				RenderCommand.setClearColor(Vec4(0.1f, 0.1f, 0.1f, 1f))
				RenderCommand.clear()
			}

			rotation += timeStep.inSeconds * 50f.degrees
			Hazel.profile("Renderer Draw") {
				with(Renderer2D) {
					beginScene(cameraController.camera)

					drawRotatedQuad(Vec2(1f, 0f), Vec2(0.8f, 0.8f), (-30f).degrees, Vec4(0.8f, 0.2f, 0.3f, 1f))
					drawQuad(Vec2(-1f, 0f), Vec2(0.8f, 0.8f), Vec4(0.8f, 0.2f, 0.3f, 1f))
					drawQuad(Vec2(0.5f, -0.5f), Vec2(0.5f, 0.75f), squareColor)
					drawQuad(Vec3(0f, 0f, -0.1f), Vec2(20f, 20f), checkerBoardTexture, 10f)
					drawRotatedQuad(Vec3(-2f, 0f, 0.1f), Vec2(1f, 1f), rotation, checkerBoardTexture, 20f)

					var y = -4.75f
					while (y < 5.25f) {
						var x = -4.75f
						while (x < 5.25f) {
							val color = Vec4((x + 5) / 10, 0.4f, (y + 5) / 10, 0.7f)
							drawQuad(Vec2(x, y), Vec2(0.45f, 0.45f), color)
							x += 0.5f
						}
						y += 0.5f
					}
					endScene()
				}
			}
		}
	}

	override fun onImGuiRender() {
		Hazel.profile("Sandbox2D.onImGuiRender()") {
			with(ImGui) {
				begin("Settings")

				with(Renderer2D.stats) {
					text("Renderer2D Stats")
					text("Draw calls: $drawCalls")
					text("Quad count: $quadCount")
					text("Vertices  : $vertexCount")
					text("Indices   : $indexCount")
				}

				spacing()

				colorEdit4("Square Color", squareColor.asFloatArray())
				end()
			}
		}
	}

	override fun onEvent(event: Event) {
		cameraController.onEvent(event)
	}
}
