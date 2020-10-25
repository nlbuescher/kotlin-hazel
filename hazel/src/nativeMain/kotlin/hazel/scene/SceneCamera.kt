package hazel.scene

import hazel.math.*
import hazel.renderer.*

class SceneCamera : Camera() {
	var orthographicSize: Float = 10f
		set(value) {
			field = value
			recalculateProjection()
		}

	private var orthographicNear: Float = -1f
	private var orthographicFar: Float = 1f

	private var aspectRatio: Float = 0f

	init {
		recalculateProjection()
	}

	fun setOrthographic(size: Float, nearClip: Float, farClip: Float) {
		orthographicSize = size
		orthographicNear = nearClip
		orthographicFar = farClip
		recalculateProjection()
	}

	fun setViewportSize(width: Int, height: Int) {
		aspectRatio = width.toFloat() / height.toFloat()
		recalculateProjection()
	}

	private fun recalculateProjection() {
		val left = -0.5f * aspectRatio * orthographicSize
		val right = 0.5f * aspectRatio * orthographicSize
		val bottom = -0.5f * orthographicSize
		val top = 0.5f * orthographicSize

		_projection = orthographicProjectionOf(left, right, bottom, top, orthographicNear, orthographicFar)
	}
}
