package hazel

import copengl.GL_ARRAY_BUFFER
import copengl.GL_COLOR_BUFFER_BIT
import copengl.GL_ELEMENT_ARRAY_BUFFER
import copengl.GL_FALSE
import copengl.GL_FLOAT
import copengl.GL_STATIC_DRAW
import copengl.GL_TRIANGLES
import copengl.GL_UNSIGNED_INT
import copengl.glBindBuffer
import copengl.glBindVertexArray
import copengl.glBufferData
import copengl.glClear
import copengl.glClearColor
import copengl.glDrawElements
import copengl.glEnableVertexAttribArray
import copengl.glGenBuffer
import copengl.glGenVertexArray
import copengl.glVertexAttribPointer
import kotlinx.cinterop.Arena
import kotlinx.cinterop.FloatVar
import kotlinx.cinterop.cValuesOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.invoke
import kotlinx.cinterop.placeTo
import kotlinx.cinterop.sizeOf

abstract class Application {
    private val scope = Arena()

    val window = Window().apply { setEventCallback(::onEvent) }

    private var isRunning: Boolean = true
    private val layerStack = LayerStack()
    private val imGuiLayer = ImGuiLayer()

    private var vertexArray: UInt
    private var vertexBuffer: UInt
    private var indexBuffer: UInt

    init {
        vertexArray = glGenVertexArray()
        glBindVertexArray!!(vertexArray)

        vertexBuffer = glGenBuffer()
        glBindBuffer!!(GL_ARRAY_BUFFER.convert(), vertexBuffer)

        val vertices = cValuesOf(
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f,
            0.0f, 0.5f, 0f
        )

        glBufferData!!(
            GL_ARRAY_BUFFER.convert(),
            vertices.size.convert(),
            vertices.placeTo(scope),
            GL_STATIC_DRAW.convert()
        )

        glEnableVertexAttribArray!!(0u)
        glVertexAttribPointer!!(0u, 3, GL_FLOAT.convert(), GL_FALSE.convert(), (3 * sizeOf<FloatVar>()).convert(), null)

        indexBuffer = glGenBuffer()
        glBindBuffer!!(GL_ELEMENT_ARRAY_BUFFER.convert(), indexBuffer)

        val indices = cValuesOf(0u, 1u, 2u)
        glBufferData!!(
            GL_ELEMENT_ARRAY_BUFFER.convert(),
            indices.size.convert(),
            indices.placeTo(scope),
            GL_STATIC_DRAW.convert()
        )
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

            glBindVertexArray!!(vertexArray)
            glDrawElements(GL_TRIANGLES.convert(), 3, GL_UNSIGNED_INT.convert(), null)

            layerStack.forEach { it.onUpdate() }

            imGuiLayer.begin()
            layerStack.forEach { it.onImGuiRender() }
            imGuiLayer.end()

            window.onUpdate()
        }

        scope.clear()
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
