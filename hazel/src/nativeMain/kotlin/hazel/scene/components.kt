package hazel.scene

import hazel.math.*
import kotlinx.serialization.*

@Serializable
class TagComponent(
	@SerialName("Tag")
	var tag: String,
)

@Serializable
class TransformComponent(
	@SerialName("Translation")
	val translation: Vec3 = Vec3(),
	@SerialName("Rotation")
	val rotation: Vec3 = Vec3(),
	@SerialName("Scale")
	val scale: Vec3 = Vec3(1f),
) {
	val transform: Mat4
		get() = Mat4()
			.translate(translation)
			.rotate(rotation.x, Vec3(1f, 0f, 0f))
			.rotate(rotation.y, Vec3(0f, 1f, 0f))
			.rotate(rotation.z, Vec3(0f, 0f, 1f))
			.scale(scale)
}

@Serializable
class SpriteRendererComponent(
	@SerialName("Color")
	val color: Vec4 = Vec4(1f),
)

@Serializable
class CameraComponent {
	@SerialName("Camera")
	val camera = SceneCamera()

	@SerialName("Primary")
	var isPrimary: Boolean = true

	@SerialName("FixedAspectRatio")
	var hasFixedAspectRatio: Boolean = false
}

class NativeScriptComponent(
	val instantiateScript: () -> ScriptableEntity,
) {
	var instance: ScriptableEntity? = null
}
