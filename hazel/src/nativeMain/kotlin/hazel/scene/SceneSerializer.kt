package hazel.scene

import hazel.core.*
import hazel.math.*
import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE

@Serializer(forClass = Vec3::class)
@OptIn(ExperimentalSerializationApi::class)
object Vec3Serializer : KSerializer<Vec3> {
	override fun serialize(encoder: Encoder, value: Vec3) {
		encoder.encodeSerializableValue(serializer(), value.toFloatArray())
	}

	override fun deserialize(decoder: Decoder): Vec3 {
		val (x, y, z) = decoder.decodeSerializableValue<FloatArray>(serializer())
		return Vec3(x, y, z)
	}
}


@Serializer(forClass = Vec4::class)
@OptIn(ExperimentalSerializationApi::class)
object Vec4Serializer : KSerializer<Vec4> {
	override fun serialize(encoder: Encoder, value: Vec4) {
		encoder.encodeSerializableValue(serializer(), value.toFloatArray())
	}

	override fun deserialize(decoder: Decoder): Vec4 {
		val (x, y, z, w) = decoder.decodeSerializableValue<FloatArray>(serializer())
		return Vec4(x, y, z, w)
	}
}


@ThreadLocal
@OptIn(ExperimentalSerializationApi::class)
object SceneSerializer : KSerializer<Scene> {
	@Suppress("VARIABLE_IN_SINGLETON_WITHOUT_THREAD_LOCAL")
	private var scene: Scene? = null

	override val descriptor: SerialDescriptor = buildClassSerialDescriptor("hazel.scene.Scene") {
		element<String>("Scene")
		element<List<Entity>>("Entities")
	}

	override fun serialize(encoder: Encoder, value: Scene) {
		encoder.encodeStructure(descriptor) {
			encodeStringElement(descriptor, 0, "Untitled")
			encodeSerializableElement(descriptor, 1, ListSerializer(EntitySerializer), value.filter { it.isValid })
		}
	}

	override fun deserialize(decoder: Decoder): Scene {
		scene = Scene()

		decoder.decodeStructure(descriptor) {
			while (true) {
				when (val index = decodeElementIndex(descriptor)) {
					0 -> {
						val sceneName = decodeStringElement(descriptor, index)
						Hazel.coreTrace("Deserializing scene '$sceneName'")
					}
					1 -> decodeSerializableElement(descriptor, index, ListSerializer(EntitySerializer))
					DECODE_DONE -> break
					else -> Hazel.coreError("Unexpected index: $index")
				}
			}
		}

		return scene!!
	}


	internal object EntitySerializer : KSerializer<Entity> {
		override val descriptor: SerialDescriptor = buildClassSerialDescriptor("hazel.scene.Scene.Entity") {
			element<Long>("Entity")
			element<TagComponent>("TagComponent")
			element<TransformComponent>("TransformComponent")
			element<CameraComponent>("CameraComponent")
			element<SpriteRendererComponent>("SpriteRendererComponent")
		}

		override fun serialize(encoder: Encoder, value: Entity) {
			encoder.encodeStructure(descriptor) {
				encodeLongElement(descriptor, 0, 12837192831273)

				if (value.hasComponent<TagComponent>()) {
					encodeSerializableElement(descriptor, 1, serializer(), value.getComponent<TagComponent>())
				}

				if (value.hasComponent<TransformComponent>()) {
					encodeSerializableElement(descriptor, 2, serializer(), value.getComponent<TransformComponent>())
				}

				if (value.hasComponent<CameraComponent>()) {
					encodeSerializableElement(descriptor, 3, serializer(), value.getComponent<CameraComponent>())
				}

				if (value.hasComponent<SpriteRendererComponent>()) {
					encodeSerializableElement(
						descriptor,
						4,
						serializer(),
						value.getComponent<SpriteRendererComponent>()
					)
				}
			}
		}

		override fun deserialize(decoder: Decoder): Entity {
			val scene = scene ?: error("deserializing entities without a scene is not supported")

			lateinit var entity: Entity

			var uuid: Long? = null
			var tag: TagComponent? = null
			var transform: TransformComponent? = null
			var camera: CameraComponent? = null
			var spriteRenderer: SpriteRendererComponent? = null

			decoder.decodeStructure(descriptor) {
				while (true) {
					when (val index = decodeElementIndex(descriptor)) {
						0 -> uuid = decodeLongElement(descriptor, index) // TODO
						1 -> tag = decodeSerializableElement(descriptor, index, serializer())
						2 -> transform = decodeSerializableElement(descriptor, index, serializer())
						3 -> camera = decodeSerializableElement(descriptor, index, serializer())
						4 -> spriteRenderer = decodeSerializableElement(descriptor, index, serializer())
						DECODE_DONE -> break
						else -> Hazel.coreError("Unexpected index: $index")
					}
				}
			}

			Hazel.coreTrace("Deserialized entity with ID '$uuid' and name '${tag?.tag}'")

			entity = scene.createEntity(tag?.tag)

			transform?.let {
				// entities always have transforms, so we remove it first
				entity.removeComponent<TransformComponent>()
				entity.addComponent(it)
			}

			camera?.let {
				it.camera.recalculateProjection()
				entity.addComponent(it)
			}

			spriteRenderer?.let {
				entity.addComponent(it)
			}

			return entity
		}
	}
}
