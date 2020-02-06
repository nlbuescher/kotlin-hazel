package hazel.math

import kotlin.math.sqrt

sealed class Vector3<T : Number> {
	abstract var x: T
	abstract var y: T
	abstract var z: T

	operator fun component1() = x
	operator fun component2() = y
	operator fun component3() = z

	operator fun get(index: Int): T = when (index) {
		0 -> x; 1 -> y; 2 -> z
		else -> throw IndexOutOfBoundsException()
	}

	operator fun set(index: Int, value: T) {
		when (index) {
			0 -> x = value; 1 -> y = value; 2 -> z = value
			else -> throw IndexOutOfBoundsException()
		}
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Vector3<*>) return false

		if (x != other.x) return false
		if (y != other.y) return false
		if (z != other.z) return false

		return true
	}

	override fun hashCode(): Int {
		var result = x.hashCode()
		result = 31 * result + y.hashCode()
		result = 31 * result + z.hashCode()
		return result
	}

	override fun toString(): String = "[$x, $y, $z]"

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

	abstract operator fun plusAssign(scalar: T)
	abstract operator fun minusAssign(scalar: T)
	abstract operator fun timesAssign(scalar: T)
	abstract operator fun divAssign(scalar: T)

	abstract operator fun plus(other: Vector3<T>): Vector3<T>
	abstract operator fun minus(other: Vector3<T>): Vector3<T>
	abstract operator fun times(other: Vector3<T>): Vector3<T>
	abstract operator fun div(other: Vector3<T>): Vector3<T>

	abstract operator fun plusAssign(other: Vector3<T>)
	abstract operator fun minusAssign(other: Vector3<T>)
	abstract operator fun timesAssign(other: Vector3<T>)
	abstract operator fun divAssign(other: Vector3<T>)
}

class FloatVector3(private val storage: FloatArray) : Vector3<Float>() {
	constructor(x: Float, y: Float, z: Float) : this(floatArrayOf(x, y, z))
	constructor(f: Float) : this(f, f, f)
	constructor() : this(0f)

	override var x: Float
		get() = storage[0]
		set(value) = storage.set(0, value)

	override var y: Float
		get() = storage[1]
		set(value) = storage.set(1, value)

	override var z: Float
		get() = storage[2]
		set(value) = storage.set(2, value)

	var r: Float
		get() = x
		set(value) = run { x = value }

	var g: Float
		get() = x
		set(value) = run { x = value }

	var b: Float
		get() = x
		set(value) = run { x = value }

	override fun copy(): FloatVector3 = FloatVector3(x, y, z)

	fun asFloatArray(): FloatArray = storage

	// geometric

	override val length: Float get() = sqrt(this dot this)

	override fun normalize(): FloatVector3 {
		val length = length
		return FloatVector3(x / length, y / length, z / length)
	}

	override fun dot(other: Vector3<Float>) = x * other.x + y * other.y + z * other.z

	override fun cross(other: Vector3<Float>): FloatVector3 {
		return FloatVector3(this[1] * other[2] - other[1] * this[2], this[2] * other[0] - other[2] * this[0], this[0] * other[1] - other[0] * this[1])
	}

	// arithmetic

	override fun plus(scalar: Float): FloatVector3 {
		return FloatVector3(x + scalar, y + scalar, z + scalar)
	}

	override fun minus(scalar: Float): FloatVector3 {
		return FloatVector3(x - scalar, y - scalar, z - scalar)
	}

	override fun times(scalar: Float): FloatVector3 {
		return FloatVector3(x * scalar, y * scalar, z * scalar)
	}

	override fun div(scalar: Float): FloatVector3 {
		return FloatVector3(x / scalar, y / scalar, z / scalar)
	}


	override fun plusAssign(scalar: Float) {
		x += scalar; y += scalar; z += scalar
	}

	override fun minusAssign(scalar: Float) {
		x -= scalar; y -= scalar; z -= scalar
	}

	override fun timesAssign(scalar: Float) {
		x *= scalar; y *= scalar; z *= scalar
	}

	override fun divAssign(scalar: Float) {
		x /= scalar; y /= scalar; z /= scalar
	}


	override fun plus(other: Vector3<Float>): FloatVector3 {
		return FloatVector3(x + other.x, y + other.y, z + other.z)
	}

	override fun minus(other: Vector3<Float>): FloatVector3 {
		return FloatVector3(x - other.x, y - other.y, z - other.z)
	}

	override fun times(other: Vector3<Float>): FloatVector3 {
		return FloatVector3(x * other.x, y * other.y, z * other.z)
	}

	override fun div(other: Vector3<Float>): FloatVector3 {
		return FloatVector3(x / other.x, y / other.y, z / other.z)
	}


	override fun plusAssign(other: Vector3<Float>) {
		x += other.x; y += other.y; z += other.z
	}

	override fun minusAssign(other: Vector3<Float>) {
		x -= other.x; y -= other.y; z -= other.z
	}

	override fun timesAssign(other: Vector3<Float>) {
		x *= other.x; y *= other.y; z *= other.z
	}

	override fun divAssign(other: Vector3<Float>) {
		x /= other.x; y /= other.y; z /= other.z
	}
}
