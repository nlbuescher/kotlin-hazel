package hazelnut

import hazel.core.*

class Hazelnut : Application("Hazelnut") {
	init {
		addLayer(EditorLayer())
	}
}

fun main() {
	Hazel.run(::Hazelnut)
}
