@file:Suppress("unused")

package hazel.math

import kotlin.math.*

@Suppress("MemberVisibilityCanBePrivate")
class Vec2(var x: Float, var y: Float) {
	constructor(scalar: Float = 0f) : this(scalar, scalar)

	fun copy(x: Float = this.x, y: Float = this.y): Vec2 = Vec2(x, y)

	operator fun get(index: Int): Float = when (index) {
		0 -> x; 1 -> y
		else -> throw IndexOutOfBoundsException()
	}

	operator fun set(index: Int, value: Float) = when (index) {
		0 -> x = value; 1 -> y = value
		else -> throw IndexOutOfBoundsException()
	}

	operator fun unaryPlus(): Vec2 = this
	operator fun unaryMinus(): Vec2 = Vec2(-x, -y)

	operator fun plus(scalar: Float): Vec2 = Vec2(x + scalar, y + scalar)
	operator fun minus(scalar: Float): Vec2 = Vec2(x - scalar, y - scalar)
	operator fun times(scalar: Float): Vec2 = Vec2(x * scalar, y * scalar)
	operator fun div(scalar: Float): Vec2 = Vec2(x / scalar, y / scalar)
	operator fun rem(scalar: Float): Vec2 = Vec2(x % scalar, y % scalar)

	operator fun plus(other: Vec2): Vec2 = Vec2(x + other.x, y + other.y)
	operator fun minus(other: Vec2): Vec2 = Vec2(x - other.x, y - other.y)
	operator fun times(other: Vec2): Vec2 = Vec2(x * other.x, y * other.y)
	operator fun div(other: Vec2): Vec2 = Vec2(x / other.x, y / other.y)
	operator fun rem(other: Vec2): Vec2 = Vec2(x % other.x, y % other.y)

	operator fun plusAssign(scalar: Float) {
		x += scalar
		y += scalar
	}

	operator fun minusAssign(scalar: Float) {
		x -= scalar
		y -= scalar
	}

	operator fun timesAssign(scalar: Float) {
		x *= scalar
		y *= scalar
	}

	operator fun divAssign(scalar: Float) {
		x /= scalar
		y /= scalar
	}

	operator fun remAssign(scalar: Float) {
		x %= scalar
		y %= scalar
	}


	operator fun plusAssign(other: Vec2) {
		x += other.x
		y += other.y
	}

	operator fun minusAssign(other: Vec2) {
		x -= other.x
		y -= other.y
	}

	operator fun timesAssign(other: Vec2) {
		x *= other.x
		y *= other.y
	}

	operator fun divAssign(other: Vec2) {
		x /= other.x
		y /= other.y
	}

	operator fun remAssign(other: Vec2) {
		x %= other.x
		y %= other.y
	}

	val squareMagnitude: Float get() = this dot this
	val magnitude: Float get() = sqrt(this dot this)

	val perpendicular: Vec2 get() = Vec2(-y, x)


	/** Returns the dot product of this vector and [other]. */
	infix fun dot(other: Vec2): Float = x * other.x + y * other.y

	fun normalize(): Vec2 = this / magnitude


	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		return other is Vec2 &&
			x == other.x &&
			y == other.y
	}

	override fun hashCode(): Int = y.hashCode() + 31 * x.hashCode()

	override fun toString(): String = "($x, $y)"
}

fun Vec2.toFloatArray(): FloatArray = floatArrayOf(x, y)

fun Vec2.toVec2(): Vec2 = Vec2(x, y)

fun Vec2.toVec3(): Vec3 = Vec3(x, y, 0f)


/** Returns the unsigned angle in radians between [from] and [to]. */
fun angle(from: Vec2, to: Vec2): Float {
	val divisor = sqrt(from.squareMagnitude * to.squareMagnitude)
	if (divisor == 0f) return 0f

	val dot = ((from dot to) / divisor).coerceIn(-1f, 1f)
	return acos(dot)
}

/** Returns the signed angle in radians between [from] and [to]. */
fun signedAngle(from: Vec2, to: Vec2): Float {
	val angle = angle(from, to)
	val sign = sign(from.x * to.y - from.y * to.x)
	return angle * sign
}

/** Returns the distance between [from] and [to]. */
fun distance(from: Vec2, to: Vec2): Float {
	val xDiff = from.x - to.x
	val yDiff = from.y - to.y
	return sqrt(xDiff * xDiff + yDiff * yDiff)
}

/** Linearly interpolates between vectors [from] and [to] by [ratio]. */
fun lerp(from: Vec2, to: Vec2, ratio: Float): Vec2 {
	return Vec2(
		from.x * (1 - ratio) + to.x * ratio,
		from.y * (1 - ratio) + to.y * ratio
	)
}

/** Returns a new vector made from the largest components of [a] and [b]. */
fun max(a: Vec2, b: Vec2): Vec2 {
	return Vec2(
		max(a.x, b.x),
		max(a.y, b.y)
	)
}

/** Returns a new vector made from the smallest components of [a] and [b]. */
fun min(a: Vec2, b: Vec2): Vec2 {
	return Vec2(
		min(a.x, b.x),
		min(a.y, b.y)
	)
}


operator fun Float.plus(vector: Vec2): Vec2 = Vec2(this + vector.x, this + vector.y)
operator fun Float.minus(vector: Vec2): Vec2 = Vec2(this - vector.x, this - vector.y)
operator fun Float.times(vector: Vec2): Vec2 = Vec2(this * vector.x, this * vector.y)
operator fun Float.div(vector: Vec2): Vec2 = Vec2(this / vector.x, this / vector.y)
operator fun Float.rem(vector: Vec2): Vec2 = Vec2(this % vector.x, this % vector.y)
