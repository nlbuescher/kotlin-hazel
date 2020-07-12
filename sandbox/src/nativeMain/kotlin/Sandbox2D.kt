import com.imgui.*
import com.imgui.ImGuiConfigFlags
import com.imgui.ImGuiDockNodeFlags
import com.imgui.ImGuiStyleVar
import com.imgui.ImGuiWindowFlags
import hazel.*
import hazel.core.*
import hazel.events.*
import hazel.math.*
import hazel.math.Vec2
import hazel.math.Vec4
import hazel.renderer.*
import kotlinx.cinterop.*

class Sandbox2D : Layer("Sandbox2D") {
	private val cameraController = OrthographicCameraController(1280f / 720f, allowRotation = true)
	private val particleSystem = ParticleSystem()

	private lateinit var checkerBoardTexture: Texture2D
	private val particleTemplate = object {
		var startColor = Vec4(254 / 255f, 212 / 255f, 123 / 255f, 1f)
		var endColor = Vec4(254 / 255f, 109 / 255f, 41 / 255f, 1f)
		var startSize = 0.5f
		var sizeVariation = 0.3f
		var endSize = 0f
		var lifeTime = 5f
		var velocity = Vec2()
		var velocityVariation = Vec2(3f, 1f)
		var position = Vec2()
	}


	override fun onAttach() {
		Hazel.profile("Sandbox2D.onAttach()") {
			checkerBoardTexture = Texture2D("assets/textures/checkerboard.png")
		}
	}

	override fun onDetach() {
		Hazel.profile("Sandbox2D.onDetach()") {}
	}

	private var rotation = 0f
	private val squareColor = floatArrayOf(0.2f, 0.3f, 0.8f, 1f)

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
					drawQuad(Vec2(0.5f, -0.5f), Vec2(0.5f, 0.75f), squareColor.let { Vec4(it[0], it[1], it[2], it[3]) })
					drawQuad(Vec3(0f, 0f, -0.1f), Vec2(20f, 20f), checkerBoardTexture, 10f)
					drawRotatedQuad(Vec3(-2f, 0f, 0.1f), Vec2(1f, 1f), rotation, checkerBoardTexture, 20f)

					for (y in -4.75f..4.75f step 0.5f) {
						for (x in -4.75f..4.75f step 0.5f) {
							val color = Vec4((x + 5) / 10, 0.4f, (y + 5) / 10, 0.7f)
							drawQuad(Vec2(x, y), Vec2(0.45f, 0.45f), color)
						}
					}

					endScene()
				}
			}

			if (Input.isMouseButtonPressed(MouseButton.BUTTON_LEFT)) {
				val (width, height) = Hazel.application.window.size

				val cameraBounds = cameraController.bounds
				val cameraPosition = cameraController.camera.position
				val x = Input.mouseX / width * cameraBounds.width - cameraBounds.width * 0.5f
				val y = cameraBounds.height * 0.5f - Input.mouseY / height * cameraBounds.height
				particleTemplate.position = Vec2(x + cameraPosition.x, y + cameraPosition.y)

				for (i in 0 until 50) {
					with(particleTemplate) {
						particleSystem.emit(
							position, velocity,
							startColor, endColor,
							startSize, endSize,
							velocityVariation, sizeVariation,
							lifeTime
						)
					}
				}
			}

			particleSystem.onUpdate(timeStep)
			particleSystem.onRender(cameraController.camera)
		}
	}

	private var dockSpaceOpen: Boolean = true
	private var isFullscreenPersistent: Boolean = true
	private var dockSpaceFlags: Flag<ImGuiDockNodeFlags>? = null

	override fun onImGuiRender() {
		Hazel.profile("Sandbox2D.onImGuiRender()") {
			with(ImGui) {
				val isFullScreen = isFullscreenPersistent

				var windowFlags: Flag<ImGuiWindowFlags> = ImGuiWindowFlags.MenuBar or ImGuiWindowFlags.NoDocking
				if (isFullScreen) {
					val viewport = getMainViewport()
					setNextWindowPos(viewport.pos)
					setNextWindowSize(viewport.size)
					setNextWindowViewport(viewport.id)
					pushStyleVar(ImGuiStyleVar.WindowRounding, 0f)
					pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0f)
					windowFlags = windowFlags or ImGuiWindowFlags.NoTitleBar or ImGuiWindowFlags.NoCollapse or
						ImGuiWindowFlags.NoResize or ImGuiWindowFlags.NoMove or
						ImGuiWindowFlags.NoBringToFrontOnFocus or ImGuiWindowFlags.NoNavFocus
				}

				pushStyleVar(ImGuiStyleVar.WindowPadding, com.imgui.Vec2(0f, 0f))
				begin("Dockspace Demo", ::dockSpaceOpen, windowFlags)
				popStyleVar()

				if (isFullScreen) popStyleVar(2)

				val io = getIO()
				if (ImGuiConfigFlags.DockingEnable in io.configFlags) {
					val dockspaceID = getID("Dockspace")
					dockSpace(dockspaceID, com.imgui.Vec2(0f, 0f), dockSpaceFlags)
				}
				if (beginMenuBar()) {
					if (beginMenu("File")) {
						if (menuItem("Exit")) Hazel.application.close()
						endMenu()
					}
					endMenuBar()
				}

				begin("Settings")

				with(Renderer2D.stats) {
					text("Renderer2D Stats")
					text("Draw calls: $drawCalls")
					text("Quad count: $quadCount")
					text("Vertices  : $vertexCount")
					text("Indices   : $indexCount")
				}

				spacing()

				colorEdit4("Square Color", squareColor)

				spacing()

				image(ImTextureID(checkerBoardTexture.rendererID.toLong().toCPointer()!!), com.imgui.Vec2(256f, 256f))

				end()

				end()
			}
		}
	}

	override fun onEvent(event: Event) {
		cameraController.onEvent(event)
	}
}
