package hazel.math

import kotlin.math.sqrt

sealed class Vector4<T : Number> {
    abstract var x: T
    abstract var y: T
    abstract var z: T
    abstract var w: T

    operator fun component1() = x
    operator fun component2() = y
    operator fun component3() = z
    operator fun component4() = w

    operator fun get(index: Int): T = when (index) {
        0 -> x; 1 -> y; 2 -> z; 3 -> w
        else -> throw IndexOutOfBoundsException()
    }

    operator fun set(index: Int, value: T) = when (index) {
        0 -> x = value; 1 -> y = value; 2 -> z = value; 3 -> w = value
        else -> throw IndexOutOfBoundsException()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Vector4<*>) return false

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false
        if (w != other.w) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        result = 31 * result + w.hashCode()
        return result
    }

    override fun toString() = "[$x, $y, $z, $w]"

    abstract fun copy(): Vector4<T>

    // geometric

    abstract val length: T

    abstract fun normalize(): Vector4<T>

    abstract infix fun dot(other: Vector4<T>): T

    // arithmetic

    abstract operator fun plus(scalar: T): Vector4<T>
    abstract operator fun minus(scalar: T): Vector4<T>
    abstract operator fun times(scalar: T): Vector4<T>
    abstract operator fun div(scalar: T): Vector4<T>

    abstract operator fun plusAssign(scalar: T)
    abstract operator fun minusAssign(scalar: T)
    abstract operator fun timesAssign(scalar: T)
    abstract operator fun divAssign(scalar: T)

    abstract operator fun plus(other: Vector4<T>): Vector4<T>
    abstract operator fun minus(other: Vector4<T>): Vector4<T>
    abstract operator fun times(other: Vector4<T>): Vector4<T>
    abstract operator fun div(other: Vector4<T>): Vector4<T>

    abstract operator fun plusAssign(other: Vector4<T>)
    abstract operator fun minusAssign(other: Vector4<T>)
    abstract operator fun timesAssign(other: Vector4<T>)
    abstract operator fun divAssign(other: Vector4<T>)
}

class FloatVector4(
    override var x: Float,
    override var y: Float,
    override var z: Float,
    override var w: Float
) : Vector4<Float>() {
    constructor() : this(0f, 0f, 0f, 0f)

    var r: Float
        get() = x
        set(value) = run { x = value }

    var g: Float
        get() = y
        set(value) = run { y = value }

    var b: Float
        get() = z
        set(value) = run { z = value }

    var a: Float
        get() = w
        set(value) = run { w = value }

    override fun copy() = FloatVector4(x, y, z, w)

    fun toFloatArray() = floatArrayOf(x, y, z, w)

    // geometric

    override val length: Float get() = sqrt(this dot this)

    override fun normalize(): FloatVector4 {
        val length = length
        return FloatVector4(x / length, y / length, z / length, w / length)
    }

    override fun dot(other: Vector4<Float>): Float = x * other.x + y * other.y + z * other.z + w * other.w

    // arithmetic

    override fun plus(scalar: Float) = FloatVector4(x + scalar, y + scalar, z + scalar, w + scalar)
    override fun minus(scalar: Float) = FloatVector4(x - scalar, y - scalar, z - scalar, w - scalar)
    override fun times(scalar: Float) = FloatVector4(x * scalar, y * scalar, z * scalar, w * scalar)
    override fun div(scalar: Float) = FloatVector4(x / scalar, y / scalar, z / scalar, w / scalar)

    override fun plusAssign(scalar: Float) = run { x += scalar; y += scalar; z += scalar; w += scalar }
    override fun minusAssign(scalar: Float) = run { x -= scalar; y -= scalar; z -= scalar; w -= scalar }
    override fun timesAssign(scalar: Float) = run { x *= scalar; y *= scalar; z *= scalar; w *= scalar }
    override fun divAssign(scalar: Float) = run { x /= scalar; y /= scalar; z /= scalar; w /= scalar }

    override fun plus(other: Vector4<Float>) = FloatVector4(x + other.x, y + other.y, z + other.z, w + other.w)
    override fun minus(other: Vector4<Float>) = FloatVector4(x - other.x, y - other.y, z - other.z, w - other.w)
    override fun times(other: Vector4<Float>) = FloatVector4(x * other.x, y * other.y, z * other.z, w * other.w)
    override fun div(other: Vector4<Float>) = FloatVector4(x / other.x, y / other.y, z / other.z, w / other.w)

    override fun plusAssign(other: Vector4<Float>) = run { x += other.x; y += other.y; z += other.z; w += other.w }
    override fun minusAssign(other: Vector4<Float>) = run { x -= other.x; y -= other.y; z -= other.z; w -= other.w }
    override fun timesAssign(other: Vector4<Float>) = run { x *= other.x; y *= other.y; z *= other.z; w *= other.w }
    override fun divAssign(other: Vector4<Float>) = run { x /= other.x; y /= other.y; z /= other.z; w /= other.w }
}
