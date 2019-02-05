package hazel

import cnames.structs.GLFWwindow
import glfw.*
import kotlinx.cinterop.*


var isGlfwInitialized = false

@ExperimentalUnsignedTypes
private fun errorCallback(error: Int, description: CPointer<ByteVar>?) {
    Hazel.coreError("GLFW Error ($error): ${description?.toKString()}")
}


@ExperimentalUnsignedTypes
data class WindowData(
    var title: String,
    var width: UInt,
    var height: UInt
) {
    var enableVSync: Boolean = false
    var eventCallback: ((Event) -> Unit)? = null
}


@ExperimentalUnsignedTypes
class MacosWindow(
    title: String,
    width: UInt,
    height: UInt
) : Window() {
    private val window: CPointer<GLFWwindow>
    private val data = StableRef.create(WindowData(title, width, height))

    override val title get() = data.get().title
    override val width get() = data.get().width
    override val height get() = data.get().height

    override fun setEventCallback(callback: ((Event) -> Unit)?) = run { data.get().eventCallback = callback }

    override var enableVSync
        get() = data.get().enableVSync
        set(value) {
            if (value) glfwSwapInterval(1)
            else glfwSwapInterval(0)
            data.get().enableVSync = value
        }

    init {
        data.get().title = title
        data.get().width = width
        data.get().height = height

        Hazel.coreInfo("Creating window $title ($width, $height)")

        if (!isGlfwInitialized) {
            val success = glfwInit() > 0
            Hazel.coreAssert(success, "Could not initialize GLFW!")
            glfwSetErrorCallback(staticCFunction(::errorCallback))
            isGlfwInitialized = true
        }

        window = glfwCreateWindow(width.toInt(), height.toInt(), title, null, null) ?: error("Could not create window!")
        glfwMakeContextCurrent(window)
        glfwSetWindowUserPointer(window, data.asCPointer())
        enableVSync = true

        // Set GLFW callbacks
        glfwSetWindowSizeCallback(window, staticCFunction { window, newWidth, newHeight ->
            val data = glfwGetWindowUserPointer(window)?.asStableRef<WindowData>()?.get()
            data?.width = newWidth.toUInt()
            data?.height = newHeight.toUInt()

            val event = WindowResizeEvent(newWidth.toUInt(), newHeight.toUInt())
            data?.eventCallback?.invoke(event)
        })

        glfwSetWindowCloseCallback(window, staticCFunction { window ->
            val data = glfwGetWindowUserPointer(window)?.asStableRef<WindowData>()?.get()
            val event = WindowCloseEvent()
            data?.eventCallback?.invoke(event)
        })

        glfwSetKeyCallback(window, staticCFunction { window, key, scanCode, action, mods ->
            val data = glfwGetWindowUserPointer(window)?.asStableRef<WindowData>()?.get()
            when (action) {
                GLFW_RELEASE -> data?.eventCallback?.invoke(KeyReleasedEvent(key))
                GLFW_PRESS -> data?.eventCallback?.invoke(KeyPressedEvent(key, 0))
                GLFW_REPEAT -> data?.eventCallback?.invoke(KeyPressedEvent(key, 1))
            }
        })

        glfwSetMouseButtonCallback(window, staticCFunction { window, button, action, mods ->
            val data = glfwGetWindowUserPointer(window)?.asStableRef<WindowData>()?.get()
            when (action) {
                GLFW_RELEASE -> data?.eventCallback?.invoke(MouseButtonReleasedEvent(button))
                GLFW_PRESS -> data?.eventCallback?.invoke(MouseButtonPressedEvent(button))
            }
        })

        glfwSetScrollCallback(window, staticCFunction { window, xOffset, yOffset ->
            val data = glfwGetWindowUserPointer(window)?.asStableRef<WindowData>()?.get()
            val event = MouseScrolledEvent(xOffset.toFloat(), yOffset.toFloat())
            data?.eventCallback?.invoke(event)
        })

        glfwSetCursorPosCallback(window, staticCFunction { window, x, y ->
            val data = glfwGetWindowUserPointer(window)?.asStableRef<WindowData>()?.get()
            val event = MouseMovedEvent(x.toFloat(), y.toFloat())
            data?.eventCallback?.invoke(event)
        })
    }

    override fun dispose() {
        data.dispose()
        glfwDestroyWindow(window)

        // temporary for memory leaks
        // glfwTerminate should be done on an application basis because we might open multiple windows
        glfwTerminate()
    }


    override fun onUpdate() {
        glfwPollEvents()
    }
}


@ExperimentalUnsignedTypes
actual fun createWindow(title: String, width: UInt, height: UInt): Window = MacosWindow(title, width, height)
