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
import cglfw.glfwMakeContextCurrent
import cglfw.glfwPollEvents
import cglfw.glfwSetCharCallback
import cglfw.glfwSetCursorPosCallback
import cglfw.glfwSetKeyCallback
import cglfw.glfwSetMouseButtonCallback
import cglfw.glfwSetScrollCallback
import cglfw.glfwSetWindowCloseCallback
import cglfw.glfwSetWindowSize
import cglfw.glfwSetWindowSizeCallback
import cglfw.glfwSetWindowUserPointer
import cglfw.glfwSwapBuffers
import cnames.structs.GLFWwindow
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.IntVar
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.alloc
import kotlinx.cinterop.asStableRef
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.staticCFunction
import kotlinx.cinterop.value
import kotlinx.io.core.Closeable
import kotlin.native.concurrent.ensureNeverFrozen


actual class Window @PublishedApi internal constructor(val ptr: CPointer<GLFWwindow>) : Closeable {

    init {
        ensureNeverFrozen()

        glfwMakeContextCurrent(ptr)
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
                GLFW_RELEASE -> data.eventCallback?.invoke(KeyReleasedEvent(key))
                GLFW_PRESS -> data.eventCallback?.invoke(KeyPressedEvent(key, 0))
                GLFW_REPEAT -> data.eventCallback?.invoke(KeyPressedEvent(key, 1))
            }
        })

        glfwSetCharCallback(ptr, staticCFunction { window, character ->
            val data = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
            data.eventCallback?.invoke(KeyTypedEvent(character.toInt()))
        })

        glfwSetMouseButtonCallback(ptr, staticCFunction { window, button, action, _ /*mods*/ ->
            val data = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
            when (action) {
                GLFW_RELEASE -> data.eventCallback?.invoke(MouseButtonReleasedEvent(button))
                GLFW_PRESS -> data.eventCallback?.invoke(MouseButtonPressedEvent(button))
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


    actual var position: Pair<Int, Int>
        get() = memScoped {
            val x = alloc<IntVar>()
            val y = alloc<IntVar>()
            glfwGetWindowSize(ptr, x.ptr, y.ptr)
            x.value to y.value
        }
        set(value) = glfwSetWindowSize(ptr, value.first, value.second)

    actual var size: Pair<Int, Int>
        get() = memScoped {
            val width = alloc<IntVar>()
            val height = alloc<IntVar>()
            glfwGetWindowSize(ptr, width.ptr, height.ptr)
            width.value to height.value
        }
        set(value) = glfwSetWindowSize(ptr, value.first, value.second)


    actual fun swapBuffers() = glfwSwapBuffers(ptr)


    private var eventCallback: ((Event) -> Unit)? = null
    actual fun setEventCallback(callback: (Event) -> Unit) {
        eventCallback = callback
    }


    actual fun onUpdate() {
        glfwPollEvents()
        swapBuffers()
    }


    override fun close() {
        glfwGetWindowUserPointer(ptr)!!.asStableRef<Window>().dispose()
        glfwDestroyWindow(ptr)
        //glfwTerminate() // causes segfault (exit code 139) on linux
    }


    actual companion object {
        actual operator fun invoke(width: Int, height: Int, title: String): Window {
            Hazel.coreAssert(glfwInit() == GLFW_TRUE, "Could not initialize GLFW!")

            glfwDefaultWindowHints()

            return Window(
                glfwCreateWindow(width, height, title, null, null) ?: throw Exception("Could not create window.")
            )
        }
    }
}
