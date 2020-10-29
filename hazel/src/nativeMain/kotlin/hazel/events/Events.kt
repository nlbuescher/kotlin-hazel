@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package hazel.events

import hazel.core.*
import kotlin.reflect.*

sealed class Event {
	var isHandled = false
	val name: String get() = this::class.simpleName ?: "Event"
	override fun toString(): String = name


	inline fun <reified T : Event> dispatch(noinline function: (T) -> Boolean): Boolean =
		dispatch(T::class, function)

	fun <T : Event> dispatch(klass: KClass<T>, function: (T) -> Boolean): Boolean {
		if (klass.isInstance(this)) {
			@Suppress("UNCHECKED_CAST")
			isHandled = isHandled || function(this as T)
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

class WindowResizeEvent(val width: Int, val height: Int) : AppEvent() {
	override fun toString() = "$name: $width, $height"
}

class WindowCloseEvent : AppEvent()

// Input Events
abstract class InputEvent : Event()

// Key events
abstract class KeyEvent(val key: Key) : InputEvent()

class KeyPressedEvent(key: Key, private val repeatCount: Int) : KeyEvent(key) {
	override fun toString() = "$name: $key ($repeatCount times)"
}

class KeyReleasedEvent(key: Key) : KeyEvent(key) {
	override fun toString() = "$name: $key"
}

class KeyTypedEvent(key: Key) : KeyEvent(key) {
	override fun toString() = "$name: $key"
}

// Mouse events
abstract class MouseEvent : InputEvent()

class MouseMovedEvent(val x: Float, val y: Float) : MouseEvent() {
	override fun toString() = "$name: $x, $y"
}

class MouseScrolledEvent(val xOffset: Float, val yOffset: Float) : MouseEvent() {
	override fun toString() = "$name: $xOffset, $yOffset"
}

abstract class MouseButtonEvent(val button: MouseButton) : MouseEvent() {
	override fun toString() = "$name: $button"
}

class MouseButtonPressedEvent(button: MouseButton) : MouseButtonEvent(button)

class MouseButtonReleasedEvent(button: MouseButton) : MouseButtonEvent(button)
