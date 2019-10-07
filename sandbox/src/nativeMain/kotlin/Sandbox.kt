import cglfw.glfwSetErrorCallback
import cimgui.igBegin
import cimgui.igEnd
import cimgui.igText
import hazel.Application
import hazel.Event
import hazel.Hazel
import hazel.Input
import hazel.Key
import hazel.Layer
import kotlinx.cinterop.staticCFunction
import kotlinx.cinterop.toKString


class ExampleLayer : Layer("ExampleLayer") {
    override fun onUpdate() {
        if (Input.isKeyPressed(Key.TAB)) {
            Hazel.info("Tab key is pressed")
        }
    }

    override fun onImGuiRender() {
        igBegin("Test", null, 0)
        igText("Hello, World!")
        igEnd()
    }

    override fun onEvent(event: Event) {
        // Hazel.trace("$event")
    }
}


class Sandbox : Application() {
    init {
        addLayer(ExampleLayer())
    }
}


fun main() {
    glfwSetErrorCallback(staticCFunction { error, message ->
        Hazel.error("GLFW error ($error): ${message?.toKString()}")
    })
    Sandbox()
    Hazel.run()
}
