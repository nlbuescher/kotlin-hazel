package hazel

import hazel.core.TimeStep


open class Layer(val debugName: String = "Layer") {
    open fun onAttach() {}
    open fun onDetach() {}
    open fun onUpdate(timeStep: TimeStep) {}
    open fun onImGuiRender() {}
    open fun onEvent(event: Event) {}
}

open class Overlay(debugName: String = "Overlay") : Layer(debugName)


class LayerStack : AbstractMutableCollection<Layer>() {
    private val layers = mutableListOf<Layer>()
    private val overlayIndex get() = layers.indexOfFirst { it is Overlay }.let { if (it < 0) size else it }

    override val size: Int get() = layers.size

    override fun iterator(): MutableIterator<Layer> = layers.iterator()

    override fun clear() = layers.clear()

    override fun add(element: Layer): Boolean = run { layers.add(overlayIndex, element); true }
    override fun remove(element: Layer): Boolean = layers.remove(element)
    fun add(element: Overlay): Boolean = layers.add(element)
}
