package hazel


@ExperimentalUnsignedTypes
abstract class Window(
    val title: String,
    val width: UInt,
    val height: UInt
) {
    abstract var enableVSync: Boolean
    var eventCallback: (Event) -> Boolean = { false }

    abstract val shouldClose: Boolean

    open fun onUpdate() {}

    open fun onDestroy() {}
}


@ExperimentalUnsignedTypes
expect fun createWindow(
    title: String = "Hazel Engine",
    width: UInt = 1280u,
    height: UInt = 720u
): Window