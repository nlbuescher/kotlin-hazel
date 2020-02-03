package hazel.core

open class Layer(val debugName: String = "Layer") : Disposable {
    override fun dispose() {}

    open fun onAttach() {}
    open fun onDetach() {}
    open fun onUpdate(timeStep: TimeStep) {}
    open fun onImGuiRender() {}
    open fun onEvent(event: Event) {}
}

open class Overlay(debugName: String = "Overlay") : Layer(debugName)


class LayerStack : AbstractMutableCollection<Layer>(), Disposable {
    private val layers = mutableListOf<Layer>()
    private val overlayIndex get() = layers.indexOfFirst { it is Overlay }.let { if (it < 0) size else it }

    override val size: Int get() = layers.size

    override fun dispose() = layers.forEach { it.dispose() }

    override fun iterator(): MutableIterator<Layer> = layers.iterator()

    override fun clear() = layers.clear()

    override fun add(element: Layer): Boolean = run { layers.add(overlayIndex, element); true }
    fun add(element: Overlay): Boolean = layers.add(element)

    override fun remove(element: Layer) = layers.remove(element)
}
