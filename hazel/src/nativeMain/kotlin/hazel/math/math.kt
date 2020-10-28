package hazel.math

import kotlin.math.*

/** Returns the specified numbers of degrees as radians */
val Float.degrees: Float get() = (this * PI / 180).toFloat()

/** Returns the specified numbers of radians as degrees */
val Float.radians: Float get() = (this * 180 / PI).toFloat()


fun lerp(from: Float, to: Float, ratio: Float): Float {
	return from * (1 - ratio) + to * ratio
}

fun perspectiveProjectionOf(fov: Float, aspect: Float, zNear: Float, zFar: Float): Mat4 = Mat4(0f).also {
	require(aspect != 0f)

	val tanHalfFov = tan(fov / 2)

	it[0][0] = 1f / (aspect * tanHalfFov)
	it[1][1] = 1f / tanHalfFov
	it[2][2] = -(zFar + zNear) / (zFar - zNear)
	it[2][3] = -1f
	it[3][2] = -(2f * zFar * zNear) / (zFar - zNear)
}

fun orthographicProjectionOf(
	left: Float,
	right: Float,
	bottom: Float,
	top: Float,
	zNear: Float,
	zFar: Float,
): Mat4 = Mat4(1f).also {
	it[0][0] = 2 / (right - left)
	it[1][1] = 2 / (top - bottom)
	it[2][2] = -2 / (zFar - zNear)
	it[3][0] = -(right + left) / (right - left)
	it[3][1] = -(top + bottom) / (top - bottom)
	it[3][2] = -(zFar + zNear) / (zFar - zNear)
}
