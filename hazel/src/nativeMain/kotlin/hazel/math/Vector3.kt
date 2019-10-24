package hazel.math

import kotlin.math.sqrt

abstract class Vector3<T : Number> {
    abstract var x: T
    abstract var y: T
    abstract var z: T

    operator fun component1() = x
    operator fun component2() = y
    operator fun component3() = z

    abstract operator fun get(index: Int): T
    abstract operator fun set(index: Int, value: T)

    abstract fun copy(): Vector3<T>

    // geometric

    abstract val length: T

    abstract fun normalize(): Vector3<T>

    abstract infix fun dot(other: Vector3<T>): T
    abstract infix fun cross(other: Vector3<T>): Vector3<T>

    // arithmetic

    abstract operator fun plus(scalar: T): Vector3<T>
    abstract operator fun minus(scalar: T): Vector3<T>
    abstract operator fun times(scalar: T): Vector3<T>
    abstract operator fun div(scalar: T): Vector3<T>

/*
    abstract operator fun plusAssign(scalar: T)
    abstract operator fun minusAssign(scalar: T)
    abstract operator fun timesAssign(scalar: T)
    abstract operator fun divAssign(scalar: T)
*/

    abstract operator fun plus(other: Vector3<T>): Vector3<T>
    abstract operator fun minus(other: Vector3<T>): Vector3<T>
    abstract operator fun times(other: Vector3<T>): Vector3<T>
    abstract operator fun div(other: Vector3<T>): Vector3<T>

/*
    abstract operator fun plusAssign(other: Vector3<T>)
    abstract operator fun minusAssign(other: Vector3<T>)
    abstract operator fun timesAssign(other: Vector3<T>)
    abstract operator fun divAssign(other: Vector3<T>)
*/
}

class FloatVector3(
    override var x: Float,
    override var y: Float,
    override var z: Float
) : Vector3<Float>() {
    constructor() : this(0f, 0f, 0f)

    override fun get(index: Int): Float = when (index) {
        0 -> x; 1 -> y; 2 -> z
        else -> throw IndexOutOfBoundsException()
    }

    override fun set(index: Int, value: Float) = when (index) {
        0 -> x = value; 1 -> y = value; 2 -> z = value
        else -> throw IndexOutOfBoundsException()
    }

    override fun copy() = FloatVector3(x, y, z)

    fun toFloatArray() = floatArrayOf(x, y, z)

    // geometric

    override val length: Float get() = sqrt(this dot this)

    override fun normalize(): FloatVector3 {
        val length = length
        return FloatVector3(x / length, y / length, z / length)
    }

    override fun dot(other: Vector3<Float>) = x * other.x + y * other.y + z * other.z

    override fun cross(other: Vector3<Float>) = FloatVector3(this[1] * other[2] - other[1] * this[2], this[2] * other[0] - other[2] * this[0], this[0] * other[1] - other[0] * this[1])

    // arithmetic

    override fun plus(scalar: Float) = FloatVector3(x + scalar, y + scalar, z + scalar)
    override fun minus(scalar: Float) = FloatVector3(x - scalar, y - scalar, z - scalar)
    override fun times(scalar: Float) = FloatVector3(x * scalar, y * scalar, z * scalar)
    override fun div(scalar: Float) = FloatVector3(x / scalar, y / scalar, z / scalar)

/*
    override fun plusAssign(scalar: Float) = run { x += scalar; y += scalar; z += scalar }
    override fun minusAssign(scalar: Float) = run { x -= scalar; y -= scalar; z -= scalar }
    override fun timesAssign(scalar: Float) = run { x *= scalar; y *= scalar; z *= scalar }
    override fun divAssign(scalar: Float) = run { x /= scalar; y /= scalar; z /= scalar }
*/

    override fun plus(other: Vector3<Float>) = FloatVector3(x + other.x, y + other.y, z + other.z)
    override fun minus(other: Vector3<Float>) = FloatVector3(x - other.x, y - other.y, z - other.z)
    override fun times(other: Vector3<Float>) = FloatVector3(x * other.x, y * other.y, z * other.z)
    override fun div(other: Vector3<Float>) = FloatVector3(x / other.x, y / other.y, z / other.z)

/*
    override fun plusAssign(other: Vector3<Float>) = run { x += other.x; y += other.y; z += other.z }
    override fun minusAssign(other: Vector3<Float>) = run { x -= other.x; y -= other.y; z -= other.z }
    override fun timesAssign(other: Vector3<Float>) = run { x *= other.x; y *= other.y; z *= other.z }
    override fun divAssign(other: Vector3<Float>) = run { x /= other.x; y /= other.y; z /= other.z }
*/
}