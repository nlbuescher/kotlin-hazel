package hazel


@ExperimentalUnsignedTypes
abstract class Application {
    private var isRunning = true
    private val window: Window = createWindow()

    init {
        window.setEventCallback(::onEvent)
    }


    open fun run() {
        while (isRunning) {
            window.onUpdate()
        }
    }


    fun onEvent(event: Event) {
        Hazel.coreTrace("$event")

        when (event) {
            is WindowCloseEvent -> onWindowClose(event)
        }
    }

    fun onWindowClose(event: WindowCloseEvent) : Boolean {
        isRunning = false
        window.dispose()
        return true
    }
}
