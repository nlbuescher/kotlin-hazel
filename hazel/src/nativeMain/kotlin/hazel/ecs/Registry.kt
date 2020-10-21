package hazel.ecs

import hazel.core.*
import kotlin.reflect.*

class Registry {
	private val entities = mutableListOf<EntityId>()
	private val pools = mutableListOf<Pool<*>?>()
	private val groups = mutableListOf<GroupData>()
	private var destroyed: EntityId? = null


	private fun <T : Any> assure(type: KClass<T>): Pool<T> {
		val typeInfo = type.typeInfo
		val index = typeInfo.index

		if (index >= pools.size) {
			for (i in pools.size..index) {
				pools.add(null)
			}
		}

		val pool = pools[index]
		if (pool == null) {
			pools[index] = Pool<T>(typeInfo)
		}

		@Suppress("UNCHECKED_CAST")
		return pools[index] as Pool<T>
	}

	private fun generateIdentifier(): EntityId {
		Hazel.coreAssert(entities.size.toUInt() < ENTITY_MASK)
		val entity = EntityId(entities.size.toUInt())
		entities.add(entity)
		return entity
	}

	private fun recycleIdentifier(): EntityId {
		Hazel.coreAssert(destroyed != null)
		val current = destroyed!!.value
		val version = entities[current.toInt()].value and (VERSION_MASK shl ENTITY_SHIFT)
		destroyed = EntityId(entities[current.toInt()].value and ENTITY_MASK)
		return EntityId(current or version).also {
			entities[current.toInt()] = it
		}
	}


	fun valid(entity: EntityId): Boolean {
		val pos = (entity.value and ENTITY_MASK).toInt()
		return pos < entities.size && entities[pos] == entity
	}

	fun create(): EntityId = if (destroyed == null) generateIdentifier() else recycleIdentifier()

	inline fun <reified T : Any> add(entity: EntityId, component: T) = add(entity, component, T::class)

	fun <T : Any> add(entity: EntityId, component: T, type: KClass<T>) {
		require(valid(entity))
		assure(type).add(this, entity, component)
	}

	fun <T : Any> get(type: KClass<T>, entity: EntityId): T {
		require(valid(entity))
		return assure(type)[entity]
	}


	fun view(get: List<KClass<*>>, exclude: List<KClass<*>> = emptyList()): View {
		require(get.isNotEmpty()) { "Exclusion-only views are not supported" }
		return View(get.map { assure(it) }, exclude.map { assure(it) })
	}

	fun group(get: List<KClass<*>>, exclude: List<KClass<*>> = emptyList()): Group {
		require(get.isNotEmpty()) { "Exclusion-only groups are not supported" }
		require(get.size + exclude.size > 1) { "Single component groups are not allowed" }

		val groupPools = get.map { assure(it) }
		val groupSize = get.size + exclude.size
		val handler: GroupHandler

		val groupData = groups.find { groupData ->
			groupData.size == groupSize
				&& get.all { groupData.get(it.typeInfo) }
				&& exclude.all { groupData.exclude(it.typeInfo) }
		}

		if (groupData != null) {
			handler = groupData.group
		} else {
			val candidate = GroupData(
				groupSize,
				GroupHandler(get, exclude),
				get = { type -> get.any { type == it.typeInfo } },
				exclude = { type -> exclude.any { type == it.typeInfo } },
			)

			handler = candidate.group

			groups.add(candidate)

			get.forEach { type ->
				with(assure(type)) {
					onAdd { owner, entity ->
						handler.maybeValidIf(owner, entity, type)
					}
					onRemove { owner, entity ->
						handler.discardIf(owner, entity)
					}
				}
			}
			exclude.forEach { type ->
				with(assure(type)) {
					onRemove { owner, entity ->
						handler.maybeValidIf(owner, entity, type)
					}
					onAdd { owner, entity ->
						handler.discardIf(owner, entity)
					}
				}
			}

			for (entity in view(get, exclude)) {
				handler.current.add(entity)
			}
		}

		return Group(get, exclude, handler.current, groupPools.toList())
	}


	private class GroupHandler(
		private val get: List<KClass<*>>,
		private val exclude: List<KClass<*>>,
	) {
		val current = mutableListOf<EntityId>()

		fun <T : Any> maybeValidIf(owner: Registry, entity: EntityId, componentType: KClass<T>) {
			val isValid =
				get.all { it == componentType || entity in owner.assure(it) }
					&& exclude.all { it == componentType || entity in owner.assure(it) }

			if (isValid && entity !in current) {
				current.add(entity)
			}
		}

		fun discardIf(owner: Registry, entity: EntityId) {
			if (entity in current) {
				current.remove(entity)
			}
		}
	}

	private class GroupData(
		var size: Int,
		var group: GroupHandler,
		var get: (TypeInfo) -> Boolean,
		var exclude: (TypeInfo) -> Boolean,
	)


	companion object {
		private const val ENTITY_MASK = 0xFFFFFu
		private const val VERSION_MASK = 0xFFFu
		private const val ENTITY_SHIFT = 20
	}
}