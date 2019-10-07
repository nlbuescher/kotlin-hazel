package hazel

import copengl.GL_COLOR_BUFFER_BIT
import copengl.glClear
import copengl.glClearColor

abstract class Application {
    @ThreadLocal
    companion object {
        internal var INSTANCE: Application? = null
    }

    init {
        Hazel.coreAssert(INSTANCE == null, "Application already exists!")
        INSTANCE = this
    }

    val window = Window().apply { setEventCallback(::onEvent) }

    private var isRunning: Boolean = true
    private val layerStack = LayerStack()
    private val imGuiLayer = ImGuiLayer().also { addOverlay(it) }


    fun addLayer(layer: Layer) {
        layerStack.add(layer)
        layer.onAttach()
    }

    fun addOverlay(overlay: Overlay) {
        layerStack.add(overlay)
        overlay.onAttach()
    }


    open fun run() {
        while (isRunning) {
            glClearColor(1f, 0f, 1f, 1f)
            glClear(GL_COLOR_BUFFER_BIT)

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
