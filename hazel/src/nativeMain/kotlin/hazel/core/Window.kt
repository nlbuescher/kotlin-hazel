package hazel.core

import com.kgl.glfw.Action
import com.kgl.glfw.Glfw
import hazel.renderer.GraphicsContext
import hazel.renderer.opengl.OpenGLContext
import kotlin.native.concurrent.ensureNeverFrozen
import com.kgl.glfw.Window as GlfwWindow

class Window @PublishedApi internal constructor(val internal: GlfwWindow) : Disposable {

    private val context: GraphicsContext

    var isVSync: Boolean = false // dummy value should be overridden in init
        set(value) {
            Hazel.profile("${this::class.qualifiedName}.${::isVSync.name}.set(${Boolean::class.qualifiedName})") {
                Glfw.setSwapInterval(if (value) 1 else 0)
            }
            field = value
        }

    init {
        ensureNeverFrozen()

        context = OpenGLContext(this).also { it.init() }

        isVSync = true

        // set GLFW callbacks
        with(internal) {
            setSizeCallback { _, width, height ->
                val event = WindowResizeEvent(width, height)
                eventCallback?.invoke(event)
            }

            setCloseCallback {
                val event = WindowCloseEvent()
                eventCallback?.invoke(event)
            }

            setKeyCallback { _, key, _ /*scanCode*/, action, _ /*mods*/ ->
                when (action) {
                    Action.Release -> eventCallback?.invoke(KeyReleasedEvent(Key.fromGlfw(key)))
                    Action.Press -> eventCallback?.invoke(KeyPressedEvent(Key.fromGlfw(key), 0))
                    Action.Repeat -> eventCallback?.invoke(KeyPressedEvent(Key.fromGlfw(key), 1))
                }
            }

            setCharCallback { _, char ->
                eventCallback?.invoke(KeyTypedEvent(Key(char.toInt())))
            }

            setMouseButtonCallback { _, button, action, _ /*mods*/ ->
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
        Hazel.profile(::dispose) {
            internal.close()
        }
    }


    var position: Pair<Int, Int>
        get() = internal.position
        set(value) = run { internal.position = value }

    var size: Pair<Int, Int>
        get() = internal.size
        set(value) = run { internal.size = value }


    private var eventCallback: ((Event) -> Unit)? = null
    fun setEventCallback(callback: (Event) -> Unit) {
        eventCallback = callback
    }


    fun onUpdate() {
        Hazel.profile(::onUpdate) {
            Glfw.pollEvents()
            context.swapBuffers()
        }
    }


    companion object {
        operator fun invoke(width: Int = 1280, height: Int = 720, title: String = "Hazel Engine"): Window {
            return Hazel.profile(::Window) {
                Hazel.profile(Glfw::init) {
                    Glfw.init()
                }

                Glfw.setErrorCallback { error, message ->
                    Hazel.error { "GLFW error ($error): $message" }
                }

                val internal = Hazel.profile("Glfw create window") {
                    GlfwWindow(width, height, title) {}
                }
                Window(internal)
            }
        }
    }
}
