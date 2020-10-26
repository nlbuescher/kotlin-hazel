package hazel.scene

import hazel.core.*
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
	private val initializer: () -> Scene.ScriptableEntity
) {
	private var _instance: Scene.ScriptableEntity? = null
	val instance: Scene.ScriptableEntity get() = _instance ?: error("must call init first")

	val isInitialized: Boolean get() = _instance != null

	fun initialize() {
		_instance = initializer()
	}

	fun dispose() {
		_instance = null
	}
}
