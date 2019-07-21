import cglfw.glfwSetErrorCallback
import hazel.Application
import hazel.Event
import hazel.Hazel
import hazel.ImGuiLayer
import hazel.Layer
import kotlinx.cinterop.staticCFunction
import kotlinx.cinterop.toKString


class ExampleLayer : Layer("ExampleLayer") {
    override fun onEvent(event: Event) {
        Hazel.trace("$event")
    }
}


class Sandbox : Application() {
    init {
        addLayer(ExampleLayer())
        addOverlay(ImGuiLayer())
    }
}


fun main() {
    glfwSetErrorCallback(staticCFunction { error, message ->
        Hazel.error("GLFW error ($error): ${message?.toKString()}")
    })
    Hazel.run(Sandbox())
}
