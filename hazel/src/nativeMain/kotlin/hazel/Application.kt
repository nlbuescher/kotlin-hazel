package hazel

import hazel.math.FloatVector4
import hazel.math.degrees
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

abstract class Application {
    val window = Window().apply { setEventCallback(::onEvent) }

    private var isRunning: Boolean = true
    private val layerStack = LayerStack()
    private val imGuiLayer = ImGuiLayer()
    val camera = OrthographicCamera(-1.6f, 1.6f, -0.9f, 0.9f)

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


    fun addLayer(layer: Layer) {
        layerStack.add(layer)
        layer.onAttach()
    }

    fun addOverlay(overlay: Overlay) {
        layerStack.add(overlay)
        overlay.onAttach()
    }


    open fun run() {
        // don't add imGuiLayer to layer stack until run because ImGuiLayer requires Hazel.application to be set
        addOverlay(imGuiLayer)

        while (isRunning) {
            RenderCommand.setClearColor(FloatVector4(0.1f, 0.1f, 0.1f, 1f))
            RenderCommand.clear()

            camera.rotation = 45f.degrees

            Renderer.scene(camera) {
                submit(blueShader, squareVertexArray)
                submit(shader, vertexArray)
            }

            layerStack.forEach { it.onUpdate() }

            imGuiLayer.begin()
            layerStack.forEach { it.onImGuiRender() }
            imGuiLayer.end()

            window.onUpdate()
        }
    }


    fun onEvent(event: Event) {
        with(Event.Dispatcher(event)) {
            dispatch(::onWindowClose)
        }

        for (layer in layerStack.reversed()) {
            layer.onEvent(event)
            if (event.isHandled) break
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onWindowClose(event: WindowCloseEvent): Boolean {
        isRunning = false
        window.dispose()
        return true
    }
}
