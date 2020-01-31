package hazel.renderer

import hazel.core.Hazel
import hazel.core.profile
import hazel.math.FloatMatrix4x4
import hazel.math.FloatVector3
import hazel.math.orthographicProjectionOf

class OrthographicCamera(left: Float, right: Float, bottom: Float, top: Float) {
    var projectionMatrix: FloatMatrix4x4
        private set
    var viewMatrix: FloatMatrix4x4
        private set
    var viewProjectionMatrix: FloatMatrix4x4
        private set

    var position = FloatVector3()
        set(value) = run { field = value; recalculateViewMatrix() }
    var rotation: Float = 0f
        set(value) = run { field = value; recalculateViewMatrix() }

    init {
        val profiler = Hazel.Profiler(::OrthographicCamera)
        profiler.start()

        projectionMatrix = orthographicProjectionOf(left, right, bottom, top, -1f, 1f)
        viewMatrix = FloatMatrix4x4(1f)
        viewProjectionMatrix = projectionMatrix * viewMatrix

        profiler.stop()
    }

    fun setProjection(left: Float, right: Float, bottom: Float, top: Float) {
        Hazel.profile(::setProjection) {
            projectionMatrix = orthographicProjectionOf(left, right, bottom, top, -1f, 1f)
            viewProjectionMatrix = projectionMatrix * viewMatrix
        }
    }

    private fun recalculateViewMatrix() {
        Hazel.profile(::recalculateViewMatrix) {
            val transform = FloatMatrix4x4(1f).translate(position).rotate(rotation, FloatVector3(0f, 0f, 1f))

            viewMatrix = transform.inv()
            viewProjectionMatrix = projectionMatrix * viewMatrix
        }
    }
}
