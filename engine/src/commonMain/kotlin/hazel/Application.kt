package hazel


@ExperimentalUnsignedTypes
abstract class Application {
    val window: Window = createWindow()

    open fun run() {
        while (!window.shouldClose) {
            window.onUpdate()
        }
    }
}
