package hazelnut

import com.imgui.*
import hazel.core.*
import hazel.events.*
import hazel.imgui.*
import hazel.math.Vec2
import hazel.math.Vec4
import hazel.renderer.*
import hazel.scene.*
import hazelnut.panels.*
import kotlinx.cinterop.*
import kotlin.random.*

class EditorLayer : Layer("Editor") {
	private val cameraController = OrthographicCameraController(1280f / 720f, allowRotation = true)

	private lateinit var frameBuffer: FrameBuffer
	private lateinit var checkerBoardTexture: Texture2D

	private lateinit var activeScene: Scene
	private var squareEntity: Scene.Entity? = null
	private lateinit var cameraEntity: Scene.Entity
	private lateinit var secondCamera: Scene.Entity
	private lateinit var sceneHierarchyPanel: SceneHierarchyPanel

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

			val square = activeScene.createEntity("Green Square")
			square.addComponent(SpriteRendererComponent(Vec4(0f, 1f, 0f, 1f)))

			val redSquare = activeScene.createEntity("Red Square")
			redSquare.addComponent(SpriteRendererComponent(Vec4(1f, 0f, 0f, 1f)))

			squareEntity = square

			cameraEntity = activeScene.createEntity("Camera A")
			cameraEntity.addComponent(CameraComponent())

			secondCamera = activeScene.createEntity("Camera B")
			secondCamera.addComponent(CameraComponent().apply { isPrimary = false })

			class CameraController : Scene.ScriptableEntity() {
				override fun onCreate() {
					val transform = getComponent<TransformComponent>().transform
					transform[3][0] = Random.nextFloat() * 10f - 5f
				}

				override fun onUpdate(timeStep: TimeStep) {
					val transform = getComponent<TransformComponent>().transform
					val speed = 5f

					if (Input.isKeyPressed(Key.A))
						transform[3][0] -= speed * timeStep.inSeconds
					if (Input.isKeyPressed(Key.D))
						transform[3][0] += speed * timeStep.inSeconds
					if (Input.isKeyPressed(Key.W))
						transform[3][1] += speed * timeStep.inSeconds
					if (Input.isKeyPressed(Key.S))
						transform[3][1] -= speed * timeStep.inSeconds
				}
			}

			cameraEntity.addComponent(NativeScriptComponent(::CameraController))
			secondCamera.addComponent(NativeScriptComponent(::CameraController))

			sceneHierarchyPanel = SceneHierarchyPanel(activeScene)
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

				sceneHierarchyPanel.onImGuiRender()

				begin("Stats")
				with(Renderer2D.stats) {
					text("Renderer2D Stats")
					text("Draw calls: $drawCalls")
					text("Quad count: $quadCount")
					text("Vertices  : $vertexCount")
					text("Indices   : $indexCount")
				}

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
