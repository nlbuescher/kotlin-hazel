package hazel.scene

import hazel.core.*
import hazel.ecs.*
import kotlinx.serialization.*
import kotlin.reflect.*

@Serializable(with = SceneSerializer.EntitySerializer::class)
class Entity(internal val id: EntityId, private val scene: Scene) {
	val isValid: Boolean get() = scene.registry.valid(id)

	inline fun <reified T : Any> addComponent(component: T) = addComponent(component, T::class)
	fun <T : Any> addComponent(component: T, type: KClass<T>) {
		Hazel.coreAssert(!hasComponent(type), "Entity already has component of type '$type'!")
		scene.registry.add(type, id, component)
		scene.onComponentAdded(this, component)
	}

	inline fun <reified T : Any> getComponent() = getComponent(T::class)
	fun <T : Any> getComponent(type: KClass<T>): T {
		Hazel.coreAssert(hasComponent(type), "Entity does not have component of type '$type'!")
		return scene.registry.get(type, id)
	}

	inline fun <reified T : Any> removeComponent() = removeComponent(T::class)
	fun <T : Any> removeComponent(type: KClass<T>) {
		Hazel.coreAssert(hasComponent(type), "Entity does not have component of type '$type'!")
		return scene.registry.remove(type, id)
	}

	inline fun <reified T : Any> hasComponent() = hasComponent(T::class)
	fun <T : Any> hasComponent(type: KClass<T>): Boolean = scene.registry.has(type, id)


	override fun hashCode(): Int = id.value.toInt()

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Entity) return false

		return id == other.id && scene == other.scene
	}

	override fun toString(): String = "${id.value}"
}

