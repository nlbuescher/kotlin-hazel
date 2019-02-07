package hazel


@ExperimentalUnsignedTypes
abstract class Application {
    private var isRunning = true
    private val window: Window = createWindow()
    private val layerStack = LayerStack()

    init {
        window.setEventCallback(::onEvent)
    }


    fun addLayer(layer: Layer) = layerStack.add(layer)
    fun addOverlay(overlay: Overlay) = layerStack.add(overlay)


    open fun run() {
        while (isRunning) {
            layerStack.forEach { it.onUpdate() }

            window.onUpdate()
        }
    }


    fun onEvent(event: Event) {
        Hazel.coreTrace("$event")

        event.dispatch(::onWindowClose)

        for (layer in layerStack.reversed()) {
            layer.onEvent(event)
            if (event.isHandled) break
        }
    }

    private fun onWindowClose(event: WindowCloseEvent): Boolean {
        isRunning = false
        window.dispose()
        return true
    }
}
