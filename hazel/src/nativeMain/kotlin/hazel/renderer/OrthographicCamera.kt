package hazel.renderer

import hazel.core.*
import hazel.math.*

class OrthographicCamera(left: Float, right: Float, bottom: Float, top: Float) {
	var projectionMatrix: Mat4 = orthographicProjectionOf(left, right, bottom, top, -1f, 1f)
		private set
	var viewMatrix = Mat4()
		private set
	var viewProjectionMatrix: Mat4
		private set

	var position: Vec3 = Vec3()
		set(value) {
			field = value
			recalculateViewMatrix()
		}
	var rotation: Float = 0f
		set(value) {
			field = value
			recalculateViewMatrix()
		}

	init {
		val profiler = Hazel.Profiler("OrthographicCamera(Float, Float, Float, Float): OrthographicCamera")
		profiler.start()

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
			val transform = Mat4()
				.translate(position)
				.rotate(rotation, Vec3(0f, 0f, 1f))

			viewMatrix = transform.inverse
			viewProjectionMatrix = projectionMatrix * viewMatrix
		}
	}
}
