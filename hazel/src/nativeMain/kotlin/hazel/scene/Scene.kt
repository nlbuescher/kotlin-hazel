package hazel.scene

import hazel.core.*
import hazel.ecs.*
import hazel.math.*
import hazel.renderer.*
import kotlin.reflect.*

class Scene {
	private val registry = Registry()

	fun createEntity(name: String? = null): Entity {
		val entity = Entity(registry.create(), this)
		entity.addComponent(TagComponent(name ?: "Entity"))
		entity.addComponent(TransformComponent())
		return entity
	}

	fun onUpdate(timeStep: TimeStep) {
		// render 2D
		var mainCamera: Camera? = null
		var cameraTransform: Mat4? = null
		registry.group(listOf(TransformComponent::class, CameraComponent::class)).let { group ->
			for (entity in group) {
				val transform = group.get(TransformComponent::class, entity)
				val camera = group.get(CameraComponent::class, entity)

				if (camera.isPrimary) {
					mainCamera = camera.camera
					cameraTransform = transform.transform
					break
				}
			}
		}

		if (mainCamera != null) {
			Renderer2D.beginScene(mainCamera!!, cameraTransform!!)

			registry.group(listOf(TransformComponent::class, SpriteRendererComponent::class)).let { group ->
				group.forEach { entity ->
					val transform = group.get(TransformComponent::class, entity)
					val sprite = group.get(SpriteRendererComponent::class, entity)
					Renderer2D.drawQuad(transform.transform, sprite.color)
				}
			}

			Renderer2D.endScene()
		}
	}


	class Entity(private val id: EntityId, private val scene: Scene) {
		inline fun <reified T : Any> addComponent(component: T) = addComponent(component, T::class)
		fun <T : Any> addComponent(component: T, type: KClass<T>) {
			Hazel.coreAssert(!hasComponent(type), "Entity already has component of type '$type'!")
			scene.registry.add(type, id, component)
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
	}
}
