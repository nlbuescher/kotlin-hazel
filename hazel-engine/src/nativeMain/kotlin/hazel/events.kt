package hazel

import kotlin.reflect.KClass


sealed class Event {
    var isHandled = false
        protected set

    val name: String get() = this::class.simpleName ?: "Event"
    override fun toString(): String = name


    class Dispatcher(private val event: Event) {
        inline fun <reified T : Event> dispatch(noinline function: (T) -> Boolean): Boolean =
            dispatch(T::class, function)

        fun <T : Event> dispatch(klass: KClass<T>, function: (T) -> Boolean): Boolean {
            if (klass.isInstance(event)) {
                @Suppress("UNCHECKED_CAST")
                event.isHandled = function(event as T)
                return true
            }
            return false
        }
    }
}

// Application events
abstract class AppEvent : Event()

class AppTickEvent : AppEvent()
class AppUpdateEvent : AppEvent()
class AppRenderEvent : AppEvent()

@ExperimentalUnsignedTypes
class WindowResizeEvent(val width: Int, val height: Int) : AppEvent() {
    override fun toString() = "$name: $width, $height"
}

class WindowCloseEvent : AppEvent()


// Input Events
abstract class InputEvent : Event()

// Key events
abstract class KeyEvent(val keyCode: Int) : InputEvent()

class KeyPressedEvent(keyCode: Int, private val repeatCount: Int) : KeyEvent(keyCode) {
    override fun toString() = "$name: $keyCode ($repeatCount times)"
}

class KeyReleasedEvent(keyCode: Int) : KeyEvent(keyCode) {
    override fun toString() = "$name: $keyCode"
}

class KeyTypedEvent(keyCode: Int) : KeyEvent(keyCode) {
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
