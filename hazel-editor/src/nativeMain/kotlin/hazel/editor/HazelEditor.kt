package hazel.editor

import hazel.core.*

class HazelEditor : Application("Hazel Editor") {
	init {
		addLayer(EditorLayer())
	}
}

fun main() {
	Hazel.run(::HazelEditor)
}
