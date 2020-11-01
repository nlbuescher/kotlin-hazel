package hazel.core

import com.kgl.glfw.*
import hazel.events.*
import hazel.renderer.*
import kotlin.native.concurrent.*
import com.kgl.glfw.Window as GlfwWindow

@Suppress("unused", "MemberVisibilityCanBePrivate")
class Window @PublishedApi internal constructor(val nativeWindow: GlfwWindow) : Disposable {
	private val context: GraphicsContext

	var isVSync: Boolean = false // dummy value should be overridden in init
		set(value) {
			Hazel.profile("Window.isVSync.set(Boolean)") {
				Glfw.setSwapInterval(if (value) 1 else 0)
				field = value
			}
		}

	init {
		ensureNeverFrozen()

		context = GraphicsContext(this).apply { init() }

		isVSync = true

		// set GLFW callbacks
		with(nativeWindow) {
			setSizeCallback { _, width, height ->
				val event = WindowResizeEvent(width, height)
				eventCallback?.invoke(event)
			}

			setCloseCallback {
				val event = WindowCloseEvent()
				eventCallback?.invoke(event)
			}

			setKeyCallback { _, key, _, action, _ ->
				when (action) {
					Action.Press -> eventCallback?.invoke(KeyPressedEvent(Key.fromGlfw(key), 0))
					Action.Repeat -> eventCallback?.invoke(KeyPressedEvent(Key.fromGlfw(key), 1))
					Action.Release -> eventCallback?.invoke(KeyReleasedEvent(Key.fromGlfw(key)))
				}
			}

			setCharCallback { _, char ->
				eventCallback?.invoke(KeyTypedEvent(Key(char.toInt())))
			}

			setMouseButtonCallback { _, button, action, _ ->
				when (action) {
					Action.Release -> eventCallback?.invoke(MouseButtonReleasedEvent(MouseButton.fromGlfw(button)))
					else -> eventCallback?.invoke(MouseButtonPressedEvent(MouseButton.fromGlfw(button)))
				}
			}

			setScrollCallback { _, xOffset, yOffset ->
				eventCallback?.invoke(MouseScrolledEvent(xOffset.toFloat(), yOffset.toFloat()))
			}

			setCursorPosCallback { _, x, y ->
				eventCallback?.invoke(MouseMovedEvent(x.toFloat(), y.toFloat()))
			}
		}
	}

	override fun dispose() {
		Hazel.profile("Window.dispose()") {
			nativeWindow.close()
			windowCount -= 1

			if (windowCount == 0) {
				Glfw.terminate()
			}
		}
	}


	var position: Pair<Int, Int>
		get() = nativeWindow.position
		set(value) {
			nativeWindow.position = value
		}

	var size: Pair<Int, Int>
		get() = nativeWindow.size
		set(value) {
			nativeWindow.size = value
		}


	private var eventCallback: ((Event) -> Unit)? = null
	fun setEventCallback(callback: (Event) -> Unit) {
		eventCallback = callback
	}


	fun onUpdate() {
		Hazel.profile("Window.onUpdate()") {
			Glfw.pollEvents()
			context.swapBuffers()
		}
	}


	companion object {
		private var windowCount: Int = 0

		operator fun invoke(title: String, width: Int = 1600, height: Int = 900): Window {
			return Hazel.profile("Window(Int, Int, String): Window") {
				Hazel.coreInfo("Creating window $title ($width, $height)")

				if (windowCount == 0) {
					Hazel.profile("Glfw.init()") {
						Hazel.coreAssert(Glfw.init(), "Could not initialize GLFW!")
						Glfw.setErrorCallback { error, message ->
							Hazel.error("GLFW error ($error): $message")
						}
					}
				}

				val nativeWindow = Hazel.profile("Glfw create window") {
					with(Glfw.windowHints) {
						// enable the most recent version of OpenGL on macOS
						// (most recent is the default on other platforms)
						if (Platform.osFamily == OsFamily.MACOSX) {
							contextVersionMajor = 3
							contextVersionMinor = 2
							openGLForwardCompat = true
							openGLProfile = OpenGLProfile.Core
						}
					}
					GlfwWindow(width, height, title)
				}
				windowCount += 1
				Window(nativeWindow)
			}
		}
	}
}
