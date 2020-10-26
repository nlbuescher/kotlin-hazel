package hazel.scene

import hazel.math.*

class TagComponent(
	val tag: String,
)

class TransformComponent(
	val transform: Mat4 = Mat4(),
)

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
