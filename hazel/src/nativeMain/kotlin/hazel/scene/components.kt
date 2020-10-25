package hazel.scene

import hazel.math.*
import hazel.renderer.*

class TagComponent(
	val tag: String,
)

class TransformComponent(
	val transform: Mat4 = Mat4(),
)

class SpriteRendererComponent(
	val color: Vec4 = Vec4(1f),
)

class CameraComponent(
	val camera: Camera,
) {
	var isPrimary: Boolean = true

	constructor(projection: Mat4) : this(Camera(projection))
}
