package hazel.ecs

class View(components: List<Pool<*>>, excludes: List<Pool<*>>) {
	private val pools: List<Pool<*>> = components
	private val view: List<EntityId> = candidate()
	private val filter: List<List<EntityId>> = excludes.map { it.entities }


	private fun candidate(): List<EntityId> {
		return pools.minByOrNull { it.size }?.entities ?: emptyList()
	}

	@OptIn(ExperimentalStdlibApi::class)
	private fun unchecked(viewPool: List<EntityId>): List<List<EntityId>> {
		return buildList {
			pools.forEach { if (it != viewPool) add(it.entities) }
		}
	}

	operator fun iterator(): Iterator<EntityId> = ViewIterator(unchecked(view))


	private inner class ViewIterator(private val unchecked: List<List<EntityId>>) : Iterator<EntityId> {
		private var index: Int = 0

		private fun isValid(): Boolean {
			return index < view.size
				&& unchecked.all { view[index] in it }
				&& (filter.isEmpty() || filter.none { view[index] in it })
		}

		override fun hasNext(): Boolean = index < unchecked.size && isValid()

		override fun next(): EntityId {
			while (index < view.size && !isValid()) index += 1
			return view[index++]
		}
	}
}
