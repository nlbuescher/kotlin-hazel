import hazel.core.*

class Sandbox : Application() {
	init {
		//addLayer(ExampleLayer())
		addLayer(Sandbox2D())
	}
}

fun main() {
	Hazel.run(::Sandbox)
}
