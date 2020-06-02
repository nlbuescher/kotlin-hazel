package hazel.core

class LayerStack : AbstractMutableCollection<Layer>(), Disposable {
	private val layers = mutableListOf<Layer>()
	private val layerInsertIndex get() = layers.indexOfFirst { it is Overlay }.let { if (it < 0) size else it }

	override val size: Int get() = layers.size

	override fun dispose() = layers.forEach { it.dispose() }

	override fun iterator(): MutableIterator<Layer> = layers.iterator()

	override fun clear() = layers.clear()

	override fun add(element: Layer): Boolean = run { layers.add(layerInsertIndex, element); true }
	fun add(element: Overlay): Boolean = layers.add(element)

	override fun remove(element: Layer) = layers.remove(element)
}
