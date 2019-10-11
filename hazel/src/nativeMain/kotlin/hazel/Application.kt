package hazel

import copengl.GL_ARRAY_BUFFER
import copengl.GL_COLOR_BUFFER_BIT
import copengl.GL_ELEMENT_ARRAY_BUFFER
import copengl.GL_FLOAT
import copengl.GL_STATIC_DRAW
import copengl.GL_TRIANGLES
import copengl.GL_UNSIGNED_INT
import copengl.glClear
import copengl.glClearColor
import copengl.glDrawElements
import hazel.renderer.Shader
import kotlinx.cinterop.FloatVar
import kotlinx.cinterop.cValuesOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.sizeOf
import opengl.glBindBuffer
import opengl.glBindVertexArray
import opengl.glBufferData
import opengl.glEnableVertexAttribArray
import opengl.glGenBuffer
import opengl.glGenVertexArray
import opengl.glVertexAttribPointer

abstract class Application {
    val window = Window().apply { setEventCallback(::onEvent) }

    private var isRunning: Boolean = true
    private val layerStack = LayerStack()
    private val imGuiLayer = ImGuiLayer()

    private val vertexArray: UInt = glGenVertexArray()
    private val vertexBuffer: UInt = glGenBuffer()
    private val indexBuffer: UInt = glGenBuffer()
    private var shader: Shader

    init {
        glBindVertexArray(vertexArray)

        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer)

        val vertices = cValuesOf(
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f,
            0.0f, 0.5f, 0f
        )
        memScoped {
            glBufferData(
                GL_ARRAY_BUFFER,
                vertices.size.toLong(),
                vertices.ptr,
                GL_STATIC_DRAW
            )
        }

        glEnableVertexAttribArray(0u)
        glVertexAttribPointer(0u, 3, GL_FLOAT, false, (3 * sizeOf<FloatVar>()).toInt(), 0)

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer)

        val indices = cValuesOf(0u, 1u, 2u)
        memScoped {
            glBufferData(
                GL_ELEMENT_ARRAY_BUFFER,
                indices.size.toLong(),
                indices.ptr,
                GL_STATIC_DRAW
            )
        }

        val vertexSource = """
            #version 330 core
            
            layout(location = 0) in vec3 a_Position;
            
            void main() {
                gl_Position = vec4(a_Position + 0.5, 1.0);
            }
        """.trimIndent()

        val fragmentSource = """
            #version 330 core
            
            layout(location = 0) out vec4 color;
            
            void main() {
                color = vec4(0.8, 0.2, 0.3, 1.0);
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
            glDrawElements(GL_TRIANGLES, 3, GL_UNSIGNED_INT, null)

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
