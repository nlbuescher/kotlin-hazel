package hazel


// Events
sealed class Event {
    protected var isHandled = false

    val name get() = this::class.simpleName ?: "Event"
    override fun toString() = name
}


// Application events
abstract class AppEvent : Event()

class AppTickEvent : AppEvent()
class AppUpdateEvent : AppEvent()
class AppRenderEvent : AppEvent()

@ExperimentalUnsignedTypes
class WindowResizeEvent(private val width: UInt, private val height: UInt) : AppEvent() {
    override fun toString() = "$name: $width, $height"
}

class WindowCloseEvent : AppEvent()


// Input Events
abstract class InputEvent : Event()

// Key events
abstract class KeyEvent(protected val keyCode: Int) : InputEvent()

class KeyPressedEvent(keyCode: Int, private val repeatCount: Int) : KeyEvent(keyCode) {
    override fun toString() = "$name: $keyCode ($repeatCount times)"
}

class KeyReleasedEvent(keyCode: Int) : KeyEvent(keyCode) {
    override fun toString() = "$name: $keyCode"
}

// Mouse events
abstract class MouseEvent : InputEvent()

class MouseMovedEvent(val x: Float, val y: Float) : MouseEvent() {
    override fun toString() = "$name: $x, $y"
}

class MouseScrolledEvent(val xOffset: Float, val yOffset: Float) : MouseEvent() {
    override fun toString() = "$name: $xOffset, $yOffset"
}

abstract class MouseButtonEvent(val button: Int) : MouseEvent() {
    override fun toString() = "$name: $button"
}

class MouseButtonPressedEvent(button: Int) : MouseButtonEvent(button)

class MouseButtonReleasedEvent(button: Int) : MouseButtonEvent(button)
