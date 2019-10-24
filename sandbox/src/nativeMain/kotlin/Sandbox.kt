import hazel.Application
import hazel.Event
import hazel.Hazel
import hazel.Input
import hazel.Key
import hazel.Layer
import hazel.math.FloatVector3
import hazel.math.FloatVector4
import hazel.renderer.BufferElement
import hazel.renderer.BufferLayout
import hazel.renderer.OrthographicCamera
import hazel.renderer.RenderCommand
import hazel.renderer.Renderer
import hazel.renderer.Shader
import hazel.renderer.ShaderDataType
import hazel.renderer.VertexArray
import hazel.renderer.indexBufferOf
import hazel.renderer.vertexBufferOf

class ExampleLayer : Layer("ExampleLayer") {
    private val camera = OrthographicCamera(-1.6f, 1.6f, -0.9f, 0.9f)
    private val cameraPosition = FloatVector3()
    private val cameraMoveSpeed: Float = 0.1f
    private var cameraRotation: Float = 0f
    private val cameraRotationSpeed: Float = 0.1f

    private val shader: Shader
    private val vertexArray = VertexArray()

    init {
        val vertexBuffer = vertexBufferOf(
            -0.5f, -0.5f, 0f, 1f, 0f, 1f, 1f,
            0.5f, -0.5f, 0f, 0f, 1f, 1f, 1f,
            0.0f, 0.5f, 0f, 1f, 1f, 0f, 1f
        )
        vertexBuffer.layout = BufferLayout(
            BufferElement(ShaderDataType.Float3, "a_Position"),
            BufferElement(ShaderDataType.Float4, "a_Color")
        )
        vertexArray.addVertexBuffer(vertexBuffer)
        vertexArray.indexBuffer = indexBufferOf(0u, 1u, 2u)

        val vertexSource = """
            #version 330 core
            
            layout(location = 0) in vec3 a_Position;
            layout(location = 1) in vec4 a_Color;
            
            uniform mat4 u_ViewProjection;
            
            out vec3 v_Position;
            out vec4 v_Color;
            
            void main() {
                v_Position = a_Position;
                v_Color = a_Color;
                gl_Position = u_ViewProjection * vec4(a_Position, 1.0);
            }
        """.trimIndent()

        val fragmentSource = """
            #version 330 core
            
            layout(location = 0) out vec4 color;
            
            in vec3 v_Position;
            in vec4 v_Color;
            
            void main() {
                color = vec4(v_Position * 0.5 + 0.5, 1.0);
                color = v_Color;
            }
        """.trimIndent()

        shader = Shader(vertexSource, fragmentSource)
    }

    private val blueShader: Shader
    private val squareVertexArray = VertexArray()

    init {
        val squareVertexBuffer = vertexBufferOf(
            -0.75f, -0.75f, 0f,
            0.75f, -0.75f, 0f,
            0.75f, 0.75f, 0f,
            -0.75f, 0.75f, 0f
        )
        squareVertexBuffer.layout = BufferLayout(
            BufferElement(ShaderDataType.Float3, "a_Position")
        )
        squareVertexArray.addVertexBuffer(squareVertexBuffer)
        squareVertexArray.indexBuffer = indexBufferOf(0u, 1u, 2u, 2u, 3u, 0u)

        val blueVertexSource = """
            #version 330 core
            
            layout(location = 0) in vec3 a_Position;
            
            uniform mat4 u_ViewProjection;

            out vec3 v_Position;
            
            void main() {
                v_Position = a_Position;
                gl_Position = u_ViewProjection * vec4(a_Position, 1.0);
            }
        """.trimIndent()

        val blueFragmentSource = """
            #version 330 core
            
            layout(location = 0) out vec4 color;
            
            in vec3 v_Position;
            
            void main() {
                color = vec4(0.0, 0.0, 1.0, 1.0);
            }
        """.trimIndent()

        blueShader = Shader(blueVertexSource, blueFragmentSource)
    }


    override fun onUpdate() {
        if (Input.isKeyPressed(Key.LEFT))
            cameraPosition.x -= cameraMoveSpeed

        if (Input.isKeyPressed(Key.RIGHT))
            cameraPosition.x += cameraMoveSpeed

        if (Input.isKeyPressed(Key.DOWN))
            cameraPosition.y -= cameraMoveSpeed

        if (Input.isKeyPressed(Key.UP))
            cameraPosition.y += cameraMoveSpeed

        if (Input.isKeyPressed(Key.D))
            cameraRotation -= cameraRotationSpeed

        if (Input.isKeyPressed(Key.A))
            cameraRotation += cameraRotationSpeed

        RenderCommand.setClearColor(FloatVector4(0.1f, 0.1f, 0.1f, 1f))
        RenderCommand.clear()

        camera.position = cameraPosition
        camera.rotation = cameraRotation

        Renderer.scene(camera) {
            submit(blueShader, squareVertexArray)
            submit(shader, vertexArray)
        }
    }

    override fun onImGuiRender() {
    }

    override fun onEvent(event: Event) {}
}

class Sandbox : Application() {
    init {
        addLayer(ExampleLayer())
    }
}

fun main() {
    Hazel.run(Sandbox())
}
