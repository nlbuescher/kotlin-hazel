package hazel.renderer

import hazel.math.*
import kotlinx.serialization.*

@Serializable
open class Camera(
	@Transient
	protected var _projection: Mat4 = Mat4(),
) {
	val projection: Mat4 get() = _projection
}
