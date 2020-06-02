import com.imgui.ImGui
import hazel.events.Event
import hazel.core.Layer
import hazel.core.TimeStep
import hazel.math.*
import hazel.renderer.*

class ExampleLayer : Layer("ExampleLayer") {
	private val cameraController = OrthographicCameraController(1280f / 720f, true)

	private val shaderLibrary = ShaderLibrary()

	private val triangleShader: Shader
	private val triangleVertexArray = VertexArray()

	init {
		val vertexBuffer = vertexBufferOf(
			-0.5f, -0.5f, 0f, 1f, 0f, 1f, 1f,
			+0.5f, -0.5f, 0f, 0f, 1f, 1f, 1f,
			+0.0f, +0.5f, 0f, 1f, 1f, 0f, 1f
		)
		vertexBuffer.layout = BufferLayout(
			BufferElement(ShaderDataType.Float3, "a_Position"),
			BufferElement(ShaderDataType.Float4, "a_Color")
		)
		triangleVertexArray.addVertexBuffer(vertexBuffer)
		triangleVertexArray.indexBuffer = indexBufferOf(0u, 1u, 2u)

		val triangleVertexSource = """
            #version 330 core
            
            layout(location = 0) in vec3 a_Position;
            layout(location = 1) in vec4 a_Color;
            
            uniform mat4 u_ViewProjection;
            uniform mat4 u_Transform;
            
            out vec3 v_Position;
            out vec4 v_Color;
            
            void main() {
                v_Position = a_Position;
                v_Color = a_Color;
                gl_Position = u_ViewProjection * u_Transform * vec4(a_Position, 1.0);
            }
        """.trimIndent()

		val triangleFragmentSource = """
            #version 330 core
            
            layout(location = 0) out vec4 color;
            
            in vec3 v_Position;
            in vec4 v_Color;
            
            void main() {
                color = vec4(v_Position * 0.5 + 0.5, 1.0);
                color = v_Color;
            }
        """.trimIndent()

		triangleShader = Shader("VertexColor", triangleVertexSource, triangleFragmentSource)
	}

	private val flatColorShader: Shader
	private val texture: Texture2D
	private val chernoLogoTexture: Texture2D
	private val squareVertexArray = VertexArray()
	private val squareColor = MutableVec3(0f, 0f, 1f)

	init {
		val squareVertexBuffer = vertexBufferOf(
			-0.5f, -0.5f, 0f, 0f, 0f,
			+0.5f, -0.5f, 0f, 1f, 0f,
			+0.5f, +0.5f, 0f, 1f, 1f,
			-0.5f, +0.5f, 0f, 0f, 1f
		)
		squareVertexBuffer.layout = BufferLayout(
			BufferElement(ShaderDataType.Float3, "a_Position"),
			BufferElement(ShaderDataType.Float2, "a_TextureCoordinate")
		)
		squareVertexArray.addVertexBuffer(squareVertexBuffer)
		squareVertexArray.indexBuffer = indexBufferOf(0u, 1u, 2u, 2u, 3u, 0u)

		val flatColorVertexSource = """
            #version 330 core
            
            layout(location = 0) in vec3 a_Position;
            
            uniform mat4 u_ViewProjection;
            uniform mat4 u_Transform;

            out vec3 v_Position;
            
            void main() {
                v_Position = a_Position;
                gl_Position = u_ViewProjection * u_Transform * vec4(a_Position, 1.0);
            }
        """.trimIndent()

		val flatColorFragmentSource = """
            #version 330 core
            
            layout(location = 0) out vec4 color;
            
            in vec3 v_Position;
            
            uniform vec3 u_Color;
            
            void main() {
                color = vec4(u_Color, 1.0f);
            }
        """.trimIndent()

		flatColorShader = Shader("FlatColor", flatColorVertexSource, flatColorFragmentSource)

		val textureShader = shaderLibrary.load("assets/shaders/texture.glsl")

		texture = Texture2D("assets/textures/checkerboard.png")
		chernoLogoTexture = Texture2D("assets/textures/cherno_logo.png")

		textureShader.bind()
		textureShader["u_Texture"] = 0
	}

	override fun dispose() {
		squareVertexArray.dispose()
		chernoLogoTexture.dispose()
		texture.dispose()
		flatColorShader.dispose()
		triangleVertexArray.dispose()
		triangleShader.dispose()
		shaderLibrary.dispose()
	}

	override fun onUpdate(timeStep: TimeStep) {
		// Update
		cameraController.onUpdate(timeStep)

		// Render
		RenderCommand.setClearColor(Vec4(0.1f, 0.1f, 0.1f, 1f))
		RenderCommand.clear()

		Renderer.scene(cameraController.camera) {
			val scale = Mat4.IDENTITY.scaled(Vec3(0.1f))

			flatColorShader.bind()
			flatColorShader["u_Color"] = squareColor

			for (y in 0 until 20) {
				for (x in 0 until 20) {
					val position = Vec3(x * 0.11f, y * 0.11f, 0f)
					val transform = Mat4.IDENTITY.toMutableMat4().apply {
						translate(position)
						this *= scale
					}
					submit(flatColorShader, squareVertexArray, transform)
				}
			}

			val textureShader = shaderLibrary["Texture"]

			// square
			texture.bind()
			submit(textureShader, squareVertexArray, Mat4.IDENTITY.scaled(Vec3(1.5f)))
			chernoLogoTexture.bind()
			submit(textureShader, squareVertexArray, Mat4.IDENTITY.scaled(Vec3(1.5f)))

			// triangle
			//submit(shader, vertexArray)
		}
	}

	override fun onImGuiRender() {
		with(ImGui) {
			begin("Settings")
			colorEdit3("Square Color", squareColor.asFloatArray())
			end()
		}
	}

	override fun onEvent(event: Event) {
		cameraController.onEvent(event)
	}
}
