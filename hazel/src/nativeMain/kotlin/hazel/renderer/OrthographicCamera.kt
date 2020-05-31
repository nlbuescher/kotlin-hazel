package hazel.renderer

import hazel.core.Hazel
import hazel.core.profile
import hazel.math.Mat4
import hazel.math.Vec3
import hazel.math.orthographicProjectionOf
import hazel.math.toMutableMat4

class OrthographicCamera(left: Float, right: Float, bottom: Float, top: Float) {
	var projectionMatrix: Mat4
		private set
	var viewMatrix: Mat4
		private set
	var viewProjectionMatrix: Mat4
		private set

	var position = Vec3()
		set(value) = run { field = value; recalculateViewMatrix() }
	var rotation: Float = 0f
		set(value) = run { field = value; recalculateViewMatrix() }

	init {
		val profiler = Hazel.Profiler("OrthographicCamera(Float, Float, Float, Float): OrthographicCamera")
		profiler.start()

		projectionMatrix = orthographicProjectionOf(left, right, bottom, top, -1f, 1f)
		viewMatrix = Mat4.IDENTITY
		viewProjectionMatrix = projectionMatrix * viewMatrix

		profiler.stop()
	}

	fun setProjection(left: Float, right: Float, bottom: Float, top: Float) {
		Hazel.profile("OrthographicCamera.setProjection(Float, Float, Float, Float)") {
			projectionMatrix = orthographicProjectionOf(left, right, bottom, top, -1f, 1f)
			viewProjectionMatrix = projectionMatrix * viewMatrix
		}
	}

	private fun recalculateViewMatrix() {
		Hazel.profile("OrthographicCamera.recalculateViewMatrix()") {
			val transform = Mat4.IDENTITY.toMutableMat4().apply {
				translate(position)
				rotate(rotation, Vec3.FORWARD)
			}

			viewMatrix = transform.inverse
			viewProjectionMatrix = projectionMatrix * viewMatrix
		}
	}
}
