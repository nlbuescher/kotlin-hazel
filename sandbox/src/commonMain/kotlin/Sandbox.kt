import hazel.Application
import hazel.Event
import hazel.Hazel
import hazel.Layer


@ExperimentalUnsignedTypes
class ExampleLayer : Layer("Example") {
    override fun onUpdate() {
        Hazel.info("ExampleLayer::Update")
    }

    override fun onEvent(event: Event) {
        Hazel.trace("$event")
    }
}


@ExperimentalUnsignedTypes
class Sandbox : Application() {
    init {
        addLayer(ExampleLayer())
    }
}


@ExperimentalUnsignedTypes
fun main() {
    Hazel.run(Sandbox())
}
