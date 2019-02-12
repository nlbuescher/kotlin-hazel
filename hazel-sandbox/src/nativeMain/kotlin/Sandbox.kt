import hazel.Application
import hazel.Event
import hazel.Hazel
import hazel.Layer


class ExampleLayer : Layer("ExampleLayer") {
    override fun onEvent(event: Event) {
        Hazel.trace("$event")
    }
}


class Sandbox : Application() {
    init {
        addLayer(ExampleLayer())
    }
}


fun main() {
    Hazel.run(Sandbox())
}