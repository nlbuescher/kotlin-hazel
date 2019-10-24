package hazel

import cglfw.GLFW_PRESS
import cglfw.GLFW_RELEASE
import cglfw.GLFW_REPEAT
import cglfw.GLFW_TRUE
import cglfw.glfwCreateWindow
import cglfw.glfwDefaultWindowHints
import cglfw.glfwDestroyWindow
import cglfw.glfwGetWindowSize
import cglfw.glfwGetWindowUserPointer
import cglfw.glfwInit
import cglfw.glfwPollEvents
import cglfw.glfwSetCharCallback
import cglfw.glfwSetCursorPosCallback
import cglfw.glfwSetErrorCallback
import cglfw.glfwSetKeyCallback
import cglfw.glfwSetMouseButtonCallback
import cglfw.glfwSetScrollCallback
import cglfw.glfwSetWindowCloseCallback
import cglfw.glfwSetWindowSize
import cglfw.glfwSetWindowSizeCallback
import cglfw.glfwSetWindowUserPointer
import cglfw.glfwTerminate
import cnames.structs.GLFWwindow
import hazel.renderer.GraphicsContext
import hazel.renderer.opengl.OpenGLContext
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.IntVar
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.alloc
import kotlinx.cinterop.asStableRef
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.staticCFunction
import kotlinx.cinterop.toKString
import kotlinx.cinterop.value
import kotlin.native.concurrent.ensureNeverFrozen

class Window @PublishedApi internal constructor(val ptr: CPointer<GLFWwindow>) : Disposable {

    private val context: GraphicsContext

    init {
        ensureNeverFrozen()

        context = OpenGLContext(ptr).also { it.init() }

        glfwSetWindowUserPointer(ptr, StableRef.create(this).asCPointer())

        // set GLFW callbacks
        glfwSetWindowSizeCallback(ptr, staticCFunction { window, width, height ->
            val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
            val event = WindowResizeEvent(width, height)
            context.eventCallback?.invoke(event)
        })

        glfwSetWindowCloseCallback(ptr, staticCFunction { window ->
            val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
            val event = WindowCloseEvent()
            context.eventCallback?.invoke(event)
        })

        glfwSetKeyCallback(ptr, staticCFunction { window, key, _ /*scanCode*/, action, _ /*mods*/ ->
            val data = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
            when (action) {
                GLFW_RELEASE -> data.eventCallback?.invoke(KeyReleasedEvent(Key.fromGlfw(key)))
                GLFW_PRESS -> data.eventCallback?.invoke(KeyPressedEvent(Key.fromGlfw(key), 0))
                GLFW_REPEAT -> data.eventCallback?.invoke(KeyPressedEvent(Key.fromGlfw(key), 1))
            }
        })

        glfwSetCharCallback(ptr, staticCFunction { window, character ->
            val data = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
            data.eventCallback?.invoke(KeyTypedEvent(Key.fromGlfw(character.toInt())))
        })

        glfwSetMouseButtonCallback(ptr, staticCFunction { window, button, action, _ /*mods*/ ->
            val data = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
            when (action) {
                GLFW_RELEASE -> data.eventCallback?.invoke(MouseButtonReleasedEvent(MouseButton.fromGlfw(button)))
                GLFW_PRESS -> data.eventCallback?.invoke(MouseButtonPressedEvent(MouseButton.fromGlfw(button)))
            }
        })

        glfwSetScrollCallback(ptr, staticCFunction { window, xOffset, yOffset ->
            val data = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
            val event = MouseScrolledEvent(xOffset.toFloat(), yOffset.toFloat())
            data.eventCallback?.invoke(event)
        })

        glfwSetCursorPosCallback(ptr, staticCFunction { window, x, y ->
            val data = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
            val event = MouseMovedEvent(x.toFloat(), y.toFloat())
            data.eventCallback?.invoke(event)
        })
    }


    var position: Pair<Int, Int>
        get() = memScoped {
            val x = alloc<IntVar>()
            val y = alloc<IntVar>()
            glfwGetWindowSize(ptr, x.ptr, y.ptr)
            x.value to y.value
        }
        set(value) = glfwSetWindowSize(ptr, value.first, value.second)

    var size: Pair<Int, Int>
        get() = memScoped {
            val width = alloc<IntVar>()
            val height = alloc<IntVar>()
            glfwGetWindowSize(ptr, width.ptr, height.ptr)
            width.value to height.value
        }
        set(value) = glfwSetWindowSize(ptr, value.first, value.second)


    private var eventCallback: ((Event) -> Unit)? = null
    fun setEventCallback(callback: (Event) -> Unit) {
        eventCallback = callback
    }


    fun onUpdate() {
        glfwPollEvents()
        context.swapBuffers()
    }


    override fun dispose() {
        glfwGetWindowUserPointer(ptr)!!.asStableRef<Window>().dispose()
        glfwDestroyWindow(ptr)
        glfwTerminate() // causes segfault (exit code 139) on linux
    }


    companion object {
        operator fun invoke(width: Int = 1280, height: Int = 720, title: String = "Hazel Engine"): Window {
            Hazel.coreAssert(glfwInit() == GLFW_TRUE) { "Could not initialize GLFW!" }

            glfwSetErrorCallback(staticCFunction { error, message ->
                Hazel.error { "GLFW error ($error): ${message?.toKString()}" }
            })

            glfwDefaultWindowHints()

            val ptr = glfwCreateWindow(width, height, title, null, null) ?: throw Exception("Could not create window.")
            return Window(ptr)
        }
    }
}
