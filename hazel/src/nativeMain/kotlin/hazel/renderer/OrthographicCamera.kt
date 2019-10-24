package hazel.renderer

import hazel.math.FloatMatrix4x4
import hazel.math.FloatVector3
import hazel.math.orthographicProjectionOf

class OrthographicCamera(left: Float, right: Float, bottom: Float, top: Float) {
    val projectionMatrix = orthographicProjectionOf(left, right, bottom, top, -1f, 1f)
    var viewMatrix = FloatMatrix4x4(1f)
        private set
    var viewProjectionMatrix = projectionMatrix * viewMatrix
        private set

    var position = FloatVector3()
        set(value) = run { field = value; recalculateViewMatrix() }
    var rotation: Float = 0f
        set(value) = run { field = value; recalculateViewMatrix() }

    private fun recalculateViewMatrix() {
        val transform = FloatMatrix4x4(1f).translate(position).rotate(rotation, FloatVector3(0f, 0f, 1f))

        viewMatrix = transform.inv()
        viewProjectionMatrix = projectionMatrix * viewMatrix
    }
}
