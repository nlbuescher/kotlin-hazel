package hazelnut

import com.imgui.*
import hazel.core.*
import hazel.events.*
import hazel.imgui.*
import hazel.math.Vec2
import hazel.math.Vec4
import hazel.renderer.*
import hazel.scene.*
import kotlinx.cinterop.*

class EditorLayer : Layer("Editor") {
	private val cameraController = OrthographicCameraController(1280f / 720f, allowRotation = true)

	private lateinit var frameBuffer: FrameBuffer
	private lateinit var checkerBoardTexture: Texture2D

	private lateinit var activeScene: Scene
	private var squareEntity: Scene.Entity? = null
	private lateinit var cameraEntity: Scene.Entity
	private lateinit var secondCamera: Scene.Entity

	private var showPrimaryCamera: Boolean = true

	private var isViewportFocused: Boolean = false
	private var isViewportHovered: Boolean = false
	private var viewportSize = Vec2()


	override fun onAttach() {
		Hazel.profile("EditorLayer.onAttach()") {
			checkerBoardTexture = Texture2D("assets/textures/checkerboard.png")

			val spec = FrameBuffer.Specification(1280, 720)
			frameBuffer = FrameBuffer(spec)

			activeScene = Scene()

			val square = activeScene.createEntity("Square")
			square.addComponent(SpriteRendererComponent(Vec4(0f, 1f, 0f, 1f)))

			squareEntity = square

			cameraEntity = activeScene.createEntity("Camera")
			cameraEntity.addComponent(CameraComponent())

			secondCamera = activeScene.createEntity("Clip-Space Camera")
			secondCamera.addComponent(CameraComponent().apply { isPrimary = false })
		}
	}

	override fun onDetach() {
		Hazel.profile("EditorLayer.onDetach()") {}
	}

	override fun onUpdate(timeStep: TimeStep) {
		Hazel.profile("EditorLayer.onUpdate(TimeStep)") {
			// resize
			run {
				val spec = frameBuffer.specification
				val viewportX = viewportSize.x.toInt()
				val viewportY = viewportSize.y.toInt()
				if (viewportX > 0f && viewportY > 0f && (spec.width != viewportX || spec.height != viewportY)) {
					frameBuffer.resize(viewportX, viewportY)
					cameraController.onResize(viewportX, viewportY)

					activeScene.onViewportResize(viewportX, viewportY)
				}
			}

			// update
			if (isViewportFocused) {
				cameraController.onUpdate(timeStep)
			}

			// render
			Renderer2D.resetStats()
			frameBuffer.bind()
			RenderCommand.setClearColor(Vec4(0.1f, 0.1f, 0.1f, 1f))
			RenderCommand.clear()

			// update scene
			activeScene.onUpdate(timeStep)

			frameBuffer.unbind()
		}
	}

	private var dockSpaceOpen: Boolean = true
	private var isFullscreenPersistent: Boolean = true
	private var dockSpaceFlags: Flag<ImGuiDockNodeFlags>? = null

	override fun onImGuiRender() {
		Hazel.profile("EditorLayer.onImGuiRender()") {
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
				begin("DockSpace Demo", ::dockSpaceOpen, windowFlags)
				popStyleVar()

				if (isFullScreen) popStyleVar(2)

				val io = getIO()
				if (ImGuiConfigFlags.DockingEnable in io.configFlags) {
					val dockSpaceID = getID("DockSpace")
					dockSpace(dockSpaceID, com.imgui.Vec2(0f, 0f), dockSpaceFlags)
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

				squareEntity?.let { squareEntity ->
					separator()
					val tag = squareEntity.getComponent<TagComponent>().tag
					text(tag)

					val squareColor = squareEntity.getComponent<SpriteRendererComponent>().color
					colorEdit4("Square Color", squareColor)
					separator()
				}

				dragFloat3("Camera Transform", cameraEntity.getComponent<TransformComponent>().transform[3])

				if (checkbox("Show Primary Camera", ::showPrimaryCamera)) {
					cameraEntity.getComponent<CameraComponent>().isPrimary = showPrimaryCamera
					secondCamera.getComponent<CameraComponent>().isPrimary = !showPrimaryCamera
				}

				val camera = secondCamera.getComponent<CameraComponent>().camera
				dragFloat("Second Camera Orthographic Size", camera::orthographicSize)

				end() // Settings

				pushStyleVar(ImGuiStyleVar.WindowPadding, com.imgui.Vec2(0f, 0f))
				begin("Viewport")

				isViewportFocused = isWindowFocused()
				isViewportHovered = isWindowHovered()
				Hazel.application.imGuiLayer.blockEvents = !isViewportHovered

				val viewportPanelSize = getContentRegionAvail()
				viewportSize = Vec2(viewportPanelSize.x, viewportPanelSize.y)
				val textureID = ImTextureID(frameBuffer.colorAttachmentRendererID.toLong().toCPointer()!!)
				image(
					textureID,
					com.imgui.Vec2(viewportSize.x, viewportSize.y),
					com.imgui.Vec2(0f, 1f),
					com.imgui.Vec2(1f, 0f)
				)
				end() // Viewport
				popStyleVar()

				end() // DockSpace
			}
		}
	}

	override fun onEvent(event: Event) {
		cameraController.onEvent(event)
	}
}
