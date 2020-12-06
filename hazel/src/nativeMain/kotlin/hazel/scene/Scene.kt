package hazel.scene

import hazel.core.*
import hazel.ecs.*
import hazel.math.*
import hazel.renderer.*
import kotlinx.serialization.*
import kotlin.reflect.*

@Serializable(with = SceneSerializer::class)
class Scene : Iterable<Entity> {
	internal val registry = Registry()

	private var viewportWidth: Int = 0
	private var viewportHeight: Int = 0

	fun createEntity(name: String? = null): Entity {
		val entity = Entity(registry.create(), this)
		entity.addComponent(TagComponent(name ?: "Entity"))
		entity.addComponent(TransformComponent())
		return entity
	}

	fun destroyEntity(entity: Entity) {
		registry.destroy(entity.id)
	}

	fun onUpdate(timeStep: TimeStep) {
		// update scripts
		registry.view(listOf(NativeScriptComponent::class)).let { view ->
			view.forEach { entity ->
				val nsc = view.get(NativeScriptComponent::class, entity)
				if (nsc.instance == null) {
					nsc.instance = nsc.instantiateScript()
					nsc.instance!!.entity = Entity(entity, this)
					nsc.instance!!.onCreate()
				}

				nsc.instance!!.onUpdate(timeStep)
			}
		}

		// render 2D
		var mainCamera: Camera? = null
		var cameraTransform = Mat4()
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

		mainCamera?.let { camera ->
			Renderer2D.beginScene(camera, cameraTransform)

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

	fun onViewportResize(width: Int, height: Int) {
		viewportWidth = width
		viewportHeight = height

		// resize non-fixed aspect-ratio cameras
		registry.view(listOf(CameraComponent::class)).let { view ->
			view.forEach { entity ->
				val cameraComponent = view.get(CameraComponent::class, entity)
				if (!cameraComponent.hasFixedAspectRatio) {
					cameraComponent.camera.setViewportSize(width, height)
				}
			}
		}
	}


	internal fun <T : Any> onComponentAdded(entity: Entity, component: T) {
		when (component) {
			is TagComponent,
			is TransformComponent,
			is SpriteRendererComponent,
			is NativeScriptComponent -> {
				// nop
			}
			is CameraComponent -> {
				component.camera.setViewportSize(viewportWidth, viewportHeight)
			}
			else -> error("Unknown component type: '${component::class}'")
		}
	}


	override operator fun iterator(): Iterator<Entity> = object : Iterator<Entity> {
		private val registryIterator = registry.iterator()
		override fun hasNext(): Boolean = registryIterator.hasNext()
		override fun next() = Entity(registryIterator.next(), this@Scene)
	}
}
