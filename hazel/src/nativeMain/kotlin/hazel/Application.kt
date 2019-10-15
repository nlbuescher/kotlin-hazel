package hazel

import copengl.GL_BOOL
import copengl.GL_COLOR_BUFFER_BIT
import copengl.GL_FLOAT
import copengl.GL_INT
import copengl.GL_TRIANGLES
import copengl.GL_UNSIGNED_INT
import copengl.glClear
import copengl.glClearColor
import copengl.glDrawElements
import hazel.renderer.BufferElement
import hazel.renderer.BufferLayout
import hazel.renderer.IndexBuffer
import hazel.renderer.Shader
import hazel.renderer.ShaderDataType
import hazel.renderer.VertexBuffer
import hazel.renderer.indexBufferOf
import hazel.renderer.vertexBufferOf
import opengl.glBindVertexArray
import opengl.glEnableVertexAttribArray
import opengl.glGenVertexArray
import opengl.glVertexAttribPointer

private fun ShaderDataType.toOpenGLBaseType() = when (this) {
    ShaderDataType.Boolean -> GL_BOOL
    ShaderDataType.Int -> GL_INT
    ShaderDataType.Int2 -> GL_INT
    ShaderDataType.Int3 -> GL_INT
    ShaderDataType.Int4 -> GL_INT
    ShaderDataType.Float -> GL_FLOAT
    ShaderDataType.Float2 -> GL_FLOAT
    ShaderDataType.Float3 -> GL_FLOAT
    ShaderDataType.Float4 -> GL_FLOAT
    ShaderDataType.Mat3 -> GL_FLOAT
    ShaderDataType.Mat4 -> GL_FLOAT
}

abstract class Application {
    val window = Window().apply { setEventCallback(::onEvent) }

    private var isRunning: Boolean = true
    private val layerStack = LayerStack()
    private val imGuiLayer = ImGuiLayer()

    private val vertexArray: UInt = glGenVertexArray()
    private val shader: Shader
    private val vertexBuffer: VertexBuffer
    private val indexBuffer: IndexBuffer

    init {
        glBindVertexArray(vertexArray)

        vertexBuffer = vertexBufferOf(
            -0.5f, -0.5f, 0f, 1f, 0f, 1f, 1f,
            0.5f, -0.5f, 0f, 0f, 1f, 1f, 1f,
            0.0f, 0.5f, 0f, 1f, 1f, 0f, 1f
        )

        vertexBuffer.layout = BufferLayout(
            BufferElement(ShaderDataType.Float3, "a_Position"),
            BufferElement(ShaderDataType.Float4, "a_Color")
        )

        vertexBuffer.layout.elements.forEachIndexed { index, element ->
            glEnableVertexAttribArray(index.toUInt())
            glVertexAttribPointer(
                index.toUInt(),
                element.componentCount,
                GL_FLOAT,
                element.isNormalized,
                vertexBuffer.layout.stride,
                element.offset
            )
        }

        indexBuffer = indexBufferOf(0u, 1u, 2u)

        val vertexSource = """
            #version 330 core
            
            layout(location = 0) in vec3 a_Position;
            layout(location = 1) in vec4 a_Color;
            
            out vec3 v_Position;
            out vec4 v_Color;
            
            void main() {
                v_Position = a_Position;
                v_Color = a_Color;
                gl_Position = vec4(a_Position, 1.0);
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
            glClearColor(0.1f, 0.1f, 0.1f, 1f)
            glClear(GL_COLOR_BUFFER_BIT)

            shader.bind()
            glBindVertexArray(vertexArray)
            glDrawElements(GL_TRIANGLES, indexBuffer.count, GL_UNSIGNED_INT, null)

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
        window.close()
        return true
    }
}
