package hazel.ecs

import kotlin.reflect.*

internal class Group(
	private val handler: List<EntityId>,
	private val pools: List<Pool<*>>,
) : Iterable<EntityId> {
	fun <T : Any> get(type: KClass<T>, entity: EntityId): T {
		@Suppress("UNCHECKED_CAST")
		val pool: Pool<T> =
			pools.find { it.typeInfo.id == type.typeInfo.id } as? Pool<T>
				?: error("no pool with type '$type' found")
		return pool[entity]
	}

	override operator fun iterator(): Iterator<EntityId> = object : Iterator<EntityId> {
		private var index: Int = 0
		override fun hasNext(): Boolean = index < handler.size
		override fun next(): EntityId {
			if (!hasNext()) throw NoSuchElementException()
			return handler[index++]
		}
	}
}
