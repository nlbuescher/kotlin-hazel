package hazel.scene

import hazel.math.*

class TagComponent(
	var tag: String,
)

class TransformComponent(
	val translation: Vec3 = Vec3(),
	val rotation: Vec3 = Vec3(),
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

class SpriteRendererComponent(
	val color: Vec4 = Vec4(1f),
)

class CameraComponent {
	val camera = SceneCamera()
	var isPrimary: Boolean = true
	var hasFixedAspectRatio: Boolean = false
}

class NativeScriptComponent(
	val instantiateScript: () -> Scene.ScriptableEntity,
) {
	var instance: Scene.ScriptableEntity? = null
}
