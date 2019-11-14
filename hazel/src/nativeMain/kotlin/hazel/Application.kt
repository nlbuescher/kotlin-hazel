package hazel

import hazel.core.Disposable
import hazel.core.TimeStepUnit.SECONDS
import hazel.core.toTimeStep
import hazel.renderer.Renderer

abstract class Application : Disposable {
    val window = Window().apply { setEventCallback(::onEvent) }

    private var isRunning: Boolean = true
    private var isMinimized: Boolean = false

    private val layerStack = LayerStack()
    private val imGuiLayer = ImGuiLayer()

    private var lastFrameTime: Float = 0f

    init {
        Renderer.init()
    }

    fun addLayer(layer: Layer) {
        layerStack.add(layer)
        layer.onAttach()
    }

    fun addOverlay(overlay: Overlay) {
        layerStack.add(overlay)
        overlay.onAttach()
    }

    fun run() {
        // don't add imGuiLayer to layer stack until run because ImGuiLayer requires Hazel.application to be set
        addOverlay(imGuiLayer)

        while (isRunning) {
            val time = Hazel.time
            val timeStep = (time - lastFrameTime).toTimeStep(SECONDS)
            lastFrameTime = time

            if (!isMinimized) {
                layerStack.forEach { it.onUpdate(timeStep) }
            }

            imGuiLayer.begin()
            layerStack.forEach { it.onImGuiRender() }
            imGuiLayer.end()

            window.onUpdate()
        }
    }

    fun onEvent(event: Event) {
        event.dispatch(::onWindowResize)
        event.dispatch(::onWindowClose)

        for (layer in layerStack.reversed()) {
            layer.onEvent(event)
            if (event.isHandled) break
        }
    }

    private fun onWindowResize(event: WindowResizeEvent): Boolean {
        if (event.width == 0 || event.height == 0) {
            isMinimized = true
            return true
        }

        isMinimized = false

        Renderer.onWindowResize(event.width, event.height)

        return false
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onWindowClose(event: WindowCloseEvent): Boolean {
        isRunning = false
        return true
    }

    override fun dispose() {
        window.dispose()
    }
}
