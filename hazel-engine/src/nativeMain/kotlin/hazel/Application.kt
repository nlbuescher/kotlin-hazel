package hazel

import com.kgl.opengl.GL_COLOR_BUFFER_BIT
import com.kgl.opengl.glClear
import com.kgl.opengl.glClearColor


abstract class Application {
    private var isRunning: Boolean = true
    private val layerStack = LayerStack()
    val window = Window()

    init {
        window.setEventCallback(::onEvent)
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
        while (isRunning) {
            glClearColor(1f, 0f, 1f, 1f)
            glClear(GL_COLOR_BUFFER_BIT)

            layerStack.forEach { it.onUpdate() }

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
