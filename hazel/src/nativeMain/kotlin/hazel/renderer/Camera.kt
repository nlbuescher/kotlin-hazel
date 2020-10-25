package hazel.renderer

import hazel.math.*

open class Camera(
	projection: Mat4 = Mat4(),
) {
	protected var _projection: Mat4 = projection
	val projection: Mat4 get() = _projection
}
