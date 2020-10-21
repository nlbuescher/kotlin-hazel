package hazel.ecs

import kotlin.reflect.*

class Group(
	private val get: List<KClass<*>>,
	private val exclude: List<KClass<*>>,
	private val handler: List<EntityId>,
	private val pools: List<Pool<*>>
) {
	fun <T : Any> get(type: KClass<T>, entity: EntityId): T {
		@Suppress("UNCHECKED_CAST")
		val pool: Pool<T> =
			pools.find { it.typeInfo == type.typeInfo } as? Pool<T>
				?: error("no pool with type '$type' found")
		return pool[entity]
	}

	operator fun iterator(): Iterator<EntityId> = object : Iterator<EntityId> {
		private var index: Int = 0
		override fun hasNext(): Boolean = index < handler.size
		override fun next(): EntityId {
			if (!hasNext()) throw NoSuchElementException()
			return handler[index++]
		}
	}
}
