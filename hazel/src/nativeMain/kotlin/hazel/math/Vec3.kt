@file:Suppress("unused")

package hazel.math

import hazel.scene.*
import kotlinx.serialization.Serializable
import kotlin.math.*

@Serializable(with = Vec3Serializer::class)
class Vec3(var x: Float, var y: Float, var z: Float) {
	constructor(scalar: Float = 0f) : this(scalar, scalar, scalar)

	fun copy(x: Float = this.x, y: Float = this.y, z: Float = this.z): Vec3 = Vec3(x, y, z)

	operator fun get(index: Int): Float = when (index) {
		0 -> x; 1 -> y; 2 -> z
		else -> throw IndexOutOfBoundsException()
	}

	operator fun set(index: Int, value: Float) = when (index) {
		0 -> x = value; 1 -> y = value; 2 -> z = value
		else -> throw IndexOutOfBoundsException()
	}

	operator fun unaryPlus(): Vec3 = this
	operator fun unaryMinus(): Vec3 = Vec3(-x, -y, -z)

	operator fun plus(scalar: Float): Vec3 = Vec3(x + scalar, y + scalar, z + scalar)
	operator fun minus(scalar: Float): Vec3 = Vec3(x - scalar, y - scalar, z - scalar)
	operator fun times(scalar: Float): Vec3 = Vec3(x * scalar, y * scalar, z * scalar)
	operator fun div(scalar: Float): Vec3 = Vec3(x / scalar, y / scalar, z / scalar)
	operator fun rem(scalar: Float): Vec3 = Vec3(x % scalar, y % scalar, z % scalar)

	operator fun plus(other: Vec3): Vec3 = Vec3(x + other.x, y + other.y, z + other.z)
	operator fun minus(other: Vec3): Vec3 = Vec3(x - other.x, y - other.y, z - other.z)
	operator fun times(other: Vec3): Vec3 = Vec3(x * other.x, y * other.y, z * other.z)
	operator fun div(other: Vec3): Vec3 = Vec3(x / other.x, y / other.y, z / other.z)
	operator fun rem(other: Vec3): Vec3 = Vec3(x % other.x, y % other.y, z % other.z)

	operator fun plusAssign(scalar: Float) {
		x += scalar
		y += scalar
		z += scalar
	}

	operator fun minusAssign(scalar: Float) {
		x -= scalar
		y -= scalar
		z -= scalar
	}

	operator fun timesAssign(scalar: Float) {
		x *= scalar
		y *= scalar
		z *= scalar
	}

	operator fun divAssign(scalar: Float) {
		x /= scalar
		y /= scalar
		z /= scalar
	}

	operator fun remAssign(scalar: Float) {
		x %= scalar
		y %= scalar
		z %= scalar
	}


	operator fun plusAssign(other: Vec3) {
		x += other.x
		y += other.y
		z += other.z
	}

	operator fun minusAssign(other: Vec3) {
		x -= other.x
		y -= other.y
		z -= other.z
	}

	operator fun timesAssign(other: Vec3) {
		x *= other.x
		y *= other.y
		z *= other.z
	}

	operator fun divAssign(other: Vec3) {
		x /= other.x
		y /= other.y
		z /= other.z
	}

	operator fun remAssign(other: Vec3) {
		x %= other.x
		y %= other.y
		z %= other.z
	}

	val squareMagnitude: Float get() = this dot this
	val magnitude: Float get() = sqrt(this dot this)

	/** Returns the cross product of this and [other]. */
	infix fun cross(other: Vec3): Vec3 = Vec3(
		y * other.z - z * other.y,
		z * other.x - x * other.z,
		x * other.y - y * other.x
	)

	/** Returns the dot product of this vector and [other]. */
	infix fun dot(other: Vec3): Float = x * other.x + y * other.y + z * other.z

	fun normalize(): Vec3 = this / magnitude


	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		return other is Vec3 &&
			x == other.x &&
			y == other.y &&
			z == other.z
	}

	override fun hashCode(): Int = z.hashCode() + 31 * (y.hashCode() + 31 * x.hashCode())

	override fun toString(): String = "($x, $y, $z)"
}

fun Vec3.toFloatArray(): FloatArray = floatArrayOf(x, y, z)

fun Vec3.toVec3(): Vec3 = Vec3(x, y, z)

/** Returns the unsigned angle in radians between [from] and [to]. */
fun angle(from: Vec3, to: Vec3): Float {
	val divisor = sqrt(from.squareMagnitude * to.squareMagnitude)
	if (divisor == 0f) return 0f

	val dot = ((from dot to) / divisor).coerceIn(-1f, 1f)
	return acos(dot)
}

/** Returns the signed angle in radians between [from] and [to]. */
fun signedAngle(from: Vec3, to: Vec3, axis: Vec3): Float {
	val angle = angle(from, to)
	val xCross = from.y * to.z - from.z * to.y
	val yCross = from.z * to.x - from.x * to.z
	val zCross = from.x * to.y - from.y * to.x
	val sign = sign(axis.x * xCross + axis.y * yCross + axis.z * zCross)
	return angle * sign
}

/** Returns the distance between [from] and [to]. */
fun distance(from: Vec3, to: Vec3): Float {
	val xDiff = from.x - to.x
	val yDiff = from.y - to.y
	val zDiff = from.z - to.z
	return sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff)
}

/** Linearly interpolates between vectors [from] and [to] by [ratio]. */
fun lerp(from: Vec3, to: Vec3, ratio: Float): Vec3 {
	return Vec3(
		from.x * (1 - ratio) + to.x * ratio,
		from.y * (1 - ratio) + to.y * ratio,
		from.z * (1 - ratio) + to.z * ratio
	)
}

/** Returns a new vector made from the largest components of [a] and [b]. */
fun max(a: Vec3, b: Vec3): Vec3 {
	return Vec3(
		max(a.x, b.x),
		max(a.y, b.y),
		max(a.z, b.z)
	)
}

/** Returns a new vector made from the smallest components of [a] and [b]. */
fun min(a: Vec3, b: Vec3): Vec3 {
	return Vec3(
		min(a.x, b.x),
		min(a.y, b.y),
		min(a.z, b.z)
	)
}


operator fun Float.plus(vector: Vec3): Vec3 = Vec3(this + vector.x, this + vector.y, this + vector.z)
operator fun Float.minus(vector: Vec3): Vec3 = Vec3(this - vector.x, this - vector.y, this - vector.z)
operator fun Float.times(vector: Vec3): Vec3 = Vec3(this * vector.x, this * vector.y, this * vector.z)
operator fun Float.div(vector: Vec3): Vec3 = Vec3(this / vector.x, this / vector.y, this / vector.z)
operator fun Float.rem(vector: Vec3): Vec3 = Vec3(this % vector.x, this % vector.y, this % vector.z)
