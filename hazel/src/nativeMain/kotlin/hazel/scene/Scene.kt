package hazel.scene

import hazel.core.*
import hazel.ecs.*
import hazel.renderer.*

class Scene {
	val registry = Registry()

	fun createEntity(): EntityId = registry.create()

	fun onUpdate(timeStep: TimeStep) {
		val group = registry.group(listOf(TransformComponent::class, SpriteRendererComponent::class))
		for (entity in group) {
			val transform = group.get(TransformComponent::class, entity)
			val sprite = group.get(SpriteRendererComponent::class, entity)
			Renderer2D.drawQuad(transform.transform, sprite.color)
		}
	}
}
