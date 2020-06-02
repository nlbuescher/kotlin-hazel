package hazel.core

import hazel.events.Event

abstract class Layer(val debugName: String = "Layer") : Disposable {
	override fun dispose() {}

	open fun onAttach() {}
	open fun onDetach() {}
	open fun onUpdate(timeStep: TimeStep) {}
	open fun onImGuiRender() {}
	open fun onEvent(event: Event) {}
}

abstract class Overlay(debugName: String = "Overlay") : Layer(debugName)
