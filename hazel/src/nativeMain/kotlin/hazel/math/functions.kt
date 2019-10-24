package hazel.math

fun orthographicProjectionOf(
    left: Float,
    right: Float,
    bottom: Float,
    top: Float,
    zNear: Float,
    zFar: Float
) = FloatMatrix4x4().also {
    it[0][0] = 2f / (right - left)
    it[1][1] = 2f / (top - bottom)
    it[2][2] = -2f / (zFar - zNear)
    it[3][0] = -(right + left) / (right - left)
    it[3][1] = -(top + bottom) / (top - bottom)
    it[3][2] = -zNear / (zFar - zNear)
}
