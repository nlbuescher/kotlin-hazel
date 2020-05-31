import hazel.core.Application
import hazel.core.Hazel
import hazel.core.run

class Sandbox : Application() {
	init {
		//addLayer(ExampleLayer())
		addLayer(Sandbox2D())
	}
}

fun main() {
	Hazel.run(::Sandbox)
}
