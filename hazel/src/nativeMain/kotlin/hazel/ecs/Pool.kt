package hazel.ecs

internal class Pool<T : Any>(val typeInfo: TypeInfo) {
	private val _entities = mutableListOf<EntityId>()
	val entities get() = _entities

	private val components = mutableListOf<T>()

	val size: Int get() = entities.size


	operator fun get(entity: EntityId): T {
		require(contains(entity))
		return components[_entities.indexOf(entity)]
	}

	fun add(owner: Registry, entity: EntityId, component: T) {
		require(!contains(entity))
		components.add(component)
		_entities.add(entity)
		addListeners.forEach { listener ->
			listener(owner, entity)
		}
	}

	fun update(owner: Registry, entity: EntityId, component: T) {
		require(contains(entity))
		components[_entities.indexOf(entity)] = component
		updateListeners.forEach { listener ->
			listener(owner, entity)
		}
	}

	fun remove(owner: Registry, entity: EntityId) {
		require(contains(entity))
		val other = components.last()
		components[_entities.indexOf(entity)] = other
		components.removeLast()
		_entities.remove(entity)
		removeListeners.forEach { listener ->
			listener(owner, entity)
		}
	}

	operator fun contains(entity: EntityId): Boolean = entity in _entities

	fun indexOf(entity: EntityId): Int = _entities.indexOf(entity)


	private val addListeners = mutableListOf<(Registry, EntityId) -> Unit>()
	private val updateListeners = mutableListOf<(Registry, EntityId) -> Unit>()
	private val removeListeners = mutableListOf<(Registry, EntityId) -> Unit>()

	fun onAdd(listener: (Registry, EntityId) -> Unit) {
		addListeners.add(listener)
	}

	fun onUpdate(listener: (Registry, EntityId) -> Unit) {
		updateListeners.add(listener)
	}

	fun onRemove(listener: (Registry, EntityId) -> Unit) {
		removeListeners.add(listener)
	}
}
