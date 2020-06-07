package hazel.math

import kotlin.math.*

/** Returns the specified numbers of degrees as radians */
val Float.degrees: Float get() = (this * PI / 180).toFloat()

fun orthographicProjectionOf(
	left: Float,
	right: Float,
	bottom: Float,
	top: Float,
	zNear: Float,
	zFar: Float
): Mat4 = Mat4.IDENTITY.toMutableMat4().also {
	it[0, 0] = 2f / (right - left)
	it[1, 1] = 2f / (top - bottom)
	it[2, 2] = -2f / (zFar - zNear)
	it[3, 0] = -(right + left) / (right - left)
	it[3, 1] = -(top + bottom) / (top - bottom)
	it[3, 2] = -zNear / (zFar - zNear)
}
