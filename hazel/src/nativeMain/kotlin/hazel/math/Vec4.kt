@file:Suppress("unused")

package hazel.math

import hazel.cinterop.*
import kotlin.math.*

@Suppress("MemberVisibilityCanBePrivate")
class Vec4(
	var x: Float,
	var y: Float,
	var z: Float,
	var w: Float
) {
	constructor(v: Vector128) : this(v.getFloatAt(0), v.getFloatAt(1), v.getFloatAt(2), v.getFloatAt(3))
	constructor(scalar: Float = 0f) : this(scalar, scalar, scalar, scalar)

	fun copy(x: Float = this.x, y: Float = this.y, z: Float = this.z, w: Float = this.w): Vec4 = Vec4(x, y, z, w)

	operator fun get(index: Int): Float = when (index) {
		0 -> x; 1 -> y; 2 -> z; 3 -> w
		else -> throw IndexOutOfBoundsException()
	}

	operator fun set(index: Int, value: Float) = when (index) {
		0 -> x = value; 1 -> y = value; 2 -> z = value; 3 -> w = value
		else -> throw IndexOutOfBoundsException()
	}

	operator fun unaryPlus(): Vec4 = this
	operator fun unaryMinus(): Vec4 = Vec4(-x, -y, -z, -w)

	operator fun plus(scalar: Float): Vec4 = Vec4(
		vec4_add(vectorOf(x, y, z, w), vectorOf(scalar, scalar, scalar, scalar))
	)

	operator fun minus(scalar: Float): Vec4 = Vec4(
		vec4_sub(vectorOf(x, y, z, w), vectorOf(scalar, scalar, scalar, scalar))
	)

	operator fun times(scalar: Float): Vec4 = Vec4(
		vec4_mul(vectorOf(x, y, z, w), vectorOf(scalar, scalar, scalar, scalar))
	)

	operator fun div(scalar: Float): Vec4 = Vec4(
		vec4_div(vectorOf(x, y, z, w), vectorOf(scalar, scalar, scalar, scalar))
	)

	operator fun rem(scalar: Float): Vec4 = Vec4(
		vec4_mod(vectorOf(x, y, z, w), vectorOf(scalar, scalar, scalar, scalar))
	)

	operator fun plus(other: Vec4): Vec4 = Vec4(
		vec4_add(vectorOf(x, y, z, w), vectorOf(other.x, other.y, other.z, other.w))
	)

	operator fun minus(other: Vec4): Vec4 = Vec4(
		vec4_sub(vectorOf(x, y, z, w), vectorOf(other.x, other.y, other.z, other.w))
	)

	operator fun times(other: Vec4): Vec4 = Vec4(
		vec4_mul(vectorOf(x, y, z, w), vectorOf(other.x, other.y, other.z, other.w))
	)

	operator fun div(other: Vec4): Vec4 = Vec4(
		vec4_div(vectorOf(x, y, z, w), vectorOf(other.x, other.y, other.z, other.w))
	)

	operator fun rem(other: Vec4): Vec4 = Vec4(
		vec4_mod(vectorOf(x, y, z, w), vectorOf(other.x, other.y, other.z, other.w))
	)

	operator fun plusAssign(scalar: Float) {
		val v = vec4_add(vectorOf(x, y, z, w), vectorOf(scalar, scalar, scalar, scalar))
		x = v.getFloatAt(0); y = v.getFloatAt(1); z = v.getFloatAt(2); w = v.getFloatAt(3)
	}

	operator fun minusAssign(scalar: Float) {
		val v = vec4_sub(vectorOf(x, y, z, w), vectorOf(scalar, scalar, scalar, scalar))
		x = v.getFloatAt(0); y = v.getFloatAt(1); z = v.getFloatAt(2); w = v.getFloatAt(3)
	}

	operator fun timesAssign(scalar: Float) {
		val v = vec4_mul(vectorOf(x, y, z, w), vectorOf(scalar, scalar, scalar, scalar))
		x = v.getFloatAt(0); y = v.getFloatAt(1); z = v.getFloatAt(2); w = v.getFloatAt(3)
	}

	operator fun divAssign(scalar: Float) {
		val v = vec4_div(vectorOf(x, y, z, w), vectorOf(scalar, scalar, scalar, scalar))
		x = v.getFloatAt(0); y = v.getFloatAt(1); z = v.getFloatAt(2); w = v.getFloatAt(3)
	}

	operator fun remAssign(scalar: Float) {
		val v = vec4_mod(vectorOf(x, y, z, w), vectorOf(scalar, scalar, scalar, scalar))
		x = v.getFloatAt(0); y = v.getFloatAt(1); z = v.getFloatAt(2); w = v.getFloatAt(3)
	}


	operator fun plusAssign(other: Vec4) {
		val v = vec4_add(vectorOf(x, y, z, w), vectorOf(other.x, other.y, other.z, other.w))
		x = v.getFloatAt(0); y = v.getFloatAt(1); z = v.getFloatAt(2); w = v.getFloatAt(3)
	}

	operator fun minusAssign(other: Vec4) {
		val v = vec4_sub(vectorOf(x, y, z, w), vectorOf(other.x, other.y, other.z, other.w))
		x = v.getFloatAt(0); y = v.getFloatAt(1); z = v.getFloatAt(2); w = v.getFloatAt(3)
	}

	operator fun timesAssign(other: Vec4) {
		val v = vec4_mul(vectorOf(x, y, z, w), vectorOf(other.x, other.y, other.z, other.w))
		x = v.getFloatAt(0); y = v.getFloatAt(1); z = v.getFloatAt(2); w = v.getFloatAt(3)
	}

	operator fun divAssign(other: Vec4) {
		val v = vec4_div(vectorOf(x, y, z, w), vectorOf(other.x, other.y, other.z, other.w))
		x = v.getFloatAt(0); y = v.getFloatAt(1); z = v.getFloatAt(2); w = v.getFloatAt(3)
	}

	operator fun remAssign(other: Vec4) {
		val v = vec4_mod(vectorOf(x, y, z, w), vectorOf(other.x, other.y, other.z, other.w))
		x = v.getFloatAt(0); y = v.getFloatAt(1); z = v.getFloatAt(2); w = v.getFloatAt(3)
	}

	val squareMagnitude: Float get() = this dot this
	val magnitude: Float get() = sqrt(this dot this)

	/** Returns the dot product of this vector and [other]. */
	infix fun dot(other: Vec4): Float = x * other.x + y * other.y + z * other.z + w * other.w

	fun normalize(): Vec4 = this / magnitude


	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		return other is Vec4 &&
			x == other.x &&
			y == other.y &&
			z == other.z &&
			w == other.w
	}

	override fun hashCode(): Int = w.hashCode() + 31 * (z.hashCode() + 31 * (y.hashCode() + 31 * x.hashCode()))

	override fun toString(): String = "($x, $y, $z, $w)"
}

fun Vec4.toFloatArray(): FloatArray = floatArrayOf(x, y, z, w)

fun Vec4.toVec4(): Vec4 = Vec4(x, y, z, w)


/** Returns the distance between [from] and [to]. */
fun distance(from: Vec4, to: Vec4): Float {
	val xDiff = from.x - to.x
	val yDiff = from.y - to.y
	val zDiff = from.z - to.z
	val wDiff = from.w - to.w
	return sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff + wDiff * wDiff)
}

/** Linearly interpolates between vectors [from] and [to] by [ratio]. */
fun lerp(from: Vec4, to: Vec4, ratio: Float): Vec4 {
	return Vec4(
		from.x * (1 - ratio) + to.x * ratio,
		from.y * (1 - ratio) + to.y * ratio,
		from.z * (1 - ratio) + to.z * ratio,
		from.w * (1 - ratio) + to.w * ratio
	)
}

/** Returns a new vector made from the largest components of [a] and [b]. */
fun max(a: Vec4, b: Vec4): Vec4 {
	return Vec4(
		max(a.x, b.x),
		max(a.y, b.y),
		max(a.z, b.z),
		max(a.w, b.w)
	)
}

/** Returns a new vector made from the smallest components of [a] and [b]. */
fun min(a: Vec4, b: Vec4): Vec4 {
	return Vec4(
		min(a.x, b.x),
		min(a.y, b.y),
		min(a.z, b.z),
		min(a.w, b.w)
	)
}


operator fun Float.plus(vector: Vec4): Vec4 = Vec4(this).also { it += vector }
operator fun Float.minus(vector: Vec4): Vec4 = Vec4(this).also { it -= vector }
operator fun Float.times(vector: Vec4): Vec4 = Vec4(this).also { it *= vector }
operator fun Float.div(vector: Vec4): Vec4 = Vec4(this).also { it /= vector }
operator fun Float.rem(vector: Vec4): Vec4 = Vec4(this).also { it %= vector }
