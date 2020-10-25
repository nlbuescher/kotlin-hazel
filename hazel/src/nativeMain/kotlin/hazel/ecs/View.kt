package hazel.ecs

import kotlin.reflect.*

internal class View(components: List<Pool<*>>, excludes: List<Pool<*>>) : Iterable<EntityId> {
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

	val size: Int get() = pools.minOf { it.size }

	override operator fun iterator(): Iterator<EntityId> = ViewIterator(unchecked(view))

	operator fun contains(entity: EntityId): Boolean {
		return pools.all { entity in it } && (filter.isEmpty() || filter.none { entity in it })
	}

	fun <T : Any> get(type: KClass<T>, entity: EntityId): T {
		require(contains(entity))
		@Suppress("UNCHECKED_CAST")
		return pools.find { it.typeInfo.id == type.typeInfo.id }!![entity] as T
	}


	private inner class ViewIterator(private val unchecked: List<List<EntityId>>) : Iterator<EntityId> {
		private var index: Int = 0

		init {
			while (index < view.size && !isValid()) index += 1
		}

		private fun isValid(): Boolean {
			return unchecked.all { view[index] in it }
				&& (filter.isEmpty() || filter.none { view[index] in it })
		}

		override fun hasNext(): Boolean = index < view.size && isValid()

		override fun next(): EntityId {
			while (index < view.size && !isValid()) index += 1
			return view[index++]
		}
	}
}
