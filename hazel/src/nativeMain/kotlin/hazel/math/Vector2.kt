package hazel.math

import kotlin.math.sqrt

sealed class Vector2<T : Number> {
    abstract var x: T
    abstract var y: T

    operator fun component1() = x
    operator fun component2() = y

    operator fun get(index: Int): T = when (index) {
        0 -> x; 1 -> y
        else -> throw IndexOutOfBoundsException()
    }

    operator fun set(index: Int, value: T) = when (index) {
        0 -> x = value; 1 -> y = value
        else -> throw IndexOutOfBoundsException()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Vector3<*>) return false

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }

    override fun toString() = "[$x, $y]"

    abstract fun copy(): Vector2<T>

    // geometric

    abstract val length: T

    abstract fun normalize(): Vector2<T>

    abstract infix fun dot(other: Vector2<T>): T

    // arithmetic

    abstract operator fun plus(scalar: T): Vector2<T>
    abstract operator fun minus(scalar: T): Vector2<T>
    abstract operator fun times(scalar: T): Vector2<T>
    abstract operator fun div(scalar: T): Vector2<T>

    abstract operator fun plusAssign(scalar: T)
    abstract operator fun minusAssign(scalar: T)
    abstract operator fun timesAssign(scalar: T)
    abstract operator fun divAssign(scalar: T)

    abstract operator fun plus(other: Vector2<T>): Vector2<T>
    abstract operator fun minus(other: Vector2<T>): Vector2<T>
    abstract operator fun times(other: Vector2<T>): Vector2<T>
    abstract operator fun div(other: Vector2<T>): Vector2<T>

    abstract operator fun plusAssign(other: Vector2<T>)
    abstract operator fun minusAssign(other: Vector2<T>)
    abstract operator fun timesAssign(other: Vector2<T>)
    abstract operator fun divAssign(other: Vector2<T>)
}

class FloatVector2(private val storage: FloatArray) : Vector2<Float>() {
    constructor(x: Float, y: Float) : this(floatArrayOf(x, y))
    constructor() : this(0f, 0f)

    override var x: Float
        get() = storage[0]
        set(value) = storage.set(0, value)

    override var y: Float
        get() = storage[1]
        set(value) = storage.set(1, value)

    override fun copy() = FloatVector2(x, y)

    fun asFloatArray() = storage

    // geometric

    override val length: Float get() = sqrt(this dot this)

    override fun normalize(): FloatVector2 {
        val length = length
        return FloatVector2(x / length, y / length)
    }

    override fun dot(other: Vector2<Float>) = x * other.x + y * other.y

    // arithmetic

    override fun plus(scalar: Float) = FloatVector2(x + scalar, y + scalar)
    override fun minus(scalar: Float) = FloatVector2(x - scalar, y - scalar)
    override fun times(scalar: Float) = FloatVector2(x * scalar, y * scalar)
    override fun div(scalar: Float) = FloatVector2(x / scalar, y / scalar)

    override fun plusAssign(scalar: Float) = run { x += scalar; y += scalar }
    override fun minusAssign(scalar: Float) = run { x -= scalar; y -= scalar }
    override fun timesAssign(scalar: Float) = run { x *= scalar; y *= scalar }
    override fun divAssign(scalar: Float) = run { x /= scalar; y /= scalar }

    override fun plus(other: Vector2<Float>) = FloatVector2(x + other.x, y + other.y)
    override fun minus(other: Vector2<Float>) = FloatVector2(x - other.x, y - other.y)
    override fun times(other: Vector2<Float>) = FloatVector2(x * other.x, y * other.y)
    override fun div(other: Vector2<Float>) = FloatVector2(x / other.x, y / other.y)

    override fun plusAssign(other: Vector2<Float>) = run { x += other.x; y += other.y }
    override fun minusAssign(other: Vector2<Float>) = run { x -= other.x; y -= other.y }
    override fun timesAssign(other: Vector2<Float>) = run { x *= other.x; y *= other.y }
    override fun divAssign(other: Vector2<Float>) = run { x /= other.x; y /= other.y }
}
