package hazel


// Events
sealed class Event {
    var isHandled = false
        protected set

    val name get() = this::class.simpleName ?: "Event"
    override fun toString() = name


    @ExperimentalUnsignedTypes
    fun dispatch(function: (WindowResizeEvent) -> Boolean): Boolean {
        if (this is WindowResizeEvent) {
            isHandled = function(this)
            return true
        }
        return false
    }

    fun dispatch(function: (WindowCloseEvent) -> Boolean): Boolean {
        if (this is WindowCloseEvent) {
            isHandled = function(this)
            return true
        }
        return false
    }

    fun dispatch(function: (KeyPressedEvent) -> Boolean): Boolean {
        if (this is KeyPressedEvent) {
            isHandled = function(this)
            return true
        }
        return false
    }

    fun dispatch(function: (KeyReleasedEvent) -> Boolean): Boolean {
        if (this is KeyReleasedEvent) {
            isHandled = function(this)
            return true
        }
        return false
    }

    fun dispatch(function: (MouseMovedEvent) -> Boolean): Boolean {
        if (this is MouseMovedEvent) {
            isHandled = function(this)
            return true
        }
        return false
    }

    fun dispatch(function: (MouseScrolledEvent) -> Boolean): Boolean {
        if (this is MouseScrolledEvent) {
            isHandled = function(this)
            return true
        }
        return false
    }

    fun dispatch(function: (MouseButtonPressedEvent) -> Boolean): Boolean {
        if (this is MouseButtonPressedEvent) {
            isHandled = function(this)
            return true
        }
        return false
    }

    fun dispatch(function: (MouseButtonReleasedEvent) -> Boolean): Boolean {
        if (this is MouseButtonReleasedEvent) {
            isHandled = function(this)
            return true
        }
        return false
    }
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
