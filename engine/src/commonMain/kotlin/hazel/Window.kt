package hazel


@ExperimentalUnsignedTypes
abstract class Window {
    abstract val title: String
    abstract val width: UInt
    abstract val height: UInt
    abstract var enableVSync: Boolean

    abstract fun setEventCallback(callback: ((Event) -> Unit)?)

    open fun onUpdate() {}

    open fun dispose() {}
}


@ExperimentalUnsignedTypes
expect fun createWindow(
    title: String = "Hazel Engine",
    width: UInt = 1280u,
    height: UInt = 720u
): Window