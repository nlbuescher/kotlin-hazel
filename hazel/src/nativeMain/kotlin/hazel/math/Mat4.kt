package hazel.math

import kotlin.math.*

class Mat4(
	private var row0: Vec4,
	private var row1: Vec4,
	private var row2: Vec4,
	private var row3: Vec4
) {
	constructor(
		c00: Float, c01: Float, c02: Float, c03: Float,
		c10: Float, c11: Float, c12: Float, c13: Float,
		c20: Float, c21: Float, c22: Float, c23: Float,
		c30: Float, c31: Float, c32: Float, c33: Float
	) : this(
		Vec4(c00, c01, c02, c03),
		Vec4(c10, c11, c12, c13),
		Vec4(c20, c21, c22, c23),
		Vec4(c30, c31, c32, c33)
	)

	constructor(scalar: Float = 1f) : this(
		Vec4(scalar, 0f, 0f, 0f),
		Vec4(0f, scalar, 0f, 0f),
		Vec4(0f, 0f, scalar, 0f),
		Vec4(0f, 0f, 0f, scalar)
	)

	fun copy(
		row0: Vec4 = this.row0,
		row1: Vec4 = this.row1,
		row2: Vec4 = this.row2,
		row3: Vec4 = this.row3
	): Mat4 = Mat4(row0, row1, row2, row3)

	fun copy(
		c00: Float = row0[0], c01: Float = row0[1], c02: Float = row0[2], c03: Float = row0[3],
		c10: Float = row1[0], c11: Float = row1[1], c12: Float = row1[2], c13: Float = row1[3],
		c20: Float = row2[0], c21: Float = row2[1], c22: Float = row2[2], c23: Float = row2[3],
		c30: Float = row3[0], c31: Float = row3[1], c32: Float = row3[2], c33: Float = row3[3]
	): Mat4 = Mat4(
		c00, c01, c02, c03,
		c10, c11, c12, c13,
		c20, c21, c22, c23,
		c30, c31, c32, c33
	)

	operator fun get(row: Int): Vec4 = when (row) {
		0 -> row0; 1 -> row1; 2 -> row2; 3 -> row3
		else -> throw IndexOutOfBoundsException()
	}

	val determinant: Float
		get() {
			val subFactor0 = row2[2] * row3[3] - row3[2] * row2[3]
			val subFactor1 = row2[1] * row3[3] - row3[1] * row2[3]
			val subFactor2 = row2[1] * row3[2] - row3[1] * row2[2]
			val subFactor3 = row2[0] * row3[3] - row3[0] * row2[3]
			val subFactor4 = row2[0] * row3[2] - row3[0] * row2[2]
			val subFactor5 = row2[0] * row3[1] - row3[0] * row2[1]

			val detCof = Vec4(
				+(row1[1] * subFactor0 - row1[2] * subFactor1 + row1[3] * subFactor2),
				-(row1[0] * subFactor0 - row1[2] * subFactor3 + row1[3] * subFactor4),
				+(row1[0] * subFactor1 - row1[1] * subFactor3 + row1[3] * subFactor5),
				-(row1[0] * subFactor2 - row1[1] * subFactor4 + row1[2] * subFactor5)
			)

			return row0[0] * detCof[0] + row0[1] * detCof[1] + row0[2] * detCof[2] + row0[3] * detCof[3]
		}

	val inverse: Mat4
		get() {
			val coef00 = row2[2] * row3[3] - row3[2] * row2[3]
			val coef02 = row1[2] * row3[3] - row3[2] * row1[3]
			val coef03 = row1[2] * row2[3] - row2[2] * row1[3]

			val coef04 = row2[1] * row3[3] - row3[1] * row2[3]
			val coef06 = row1[1] * row3[3] - row3[1] * row1[3]
			val coef07 = row1[1] * row2[3] - row2[1] * row1[3]

			val coef08 = row2[1] * row3[2] - row3[1] * row2[2]
			val coef10 = row1[1] * row3[2] - row3[1] * row1[2]
			val coef11 = row1[1] * row2[2] - row2[1] * row1[2]

			val coef12 = row2[0] * row3[3] - row3[0] * row2[3]
			val coef14 = row1[0] * row3[3] - row3[0] * row1[3]
			val coef15 = row1[0] * row2[3] - row2[0] * row1[3]

			val coef16 = row2[0] * row3[2] - row3[0] * row2[2]
			val coef18 = row1[0] * row3[2] - row3[0] * row1[2]
			val coef19 = row1[0] * row2[2] - row2[0] * row1[2]

			val coef20 = row2[0] * row3[1] - row3[0] * row2[1]
			val coef22 = row1[0] * row3[1] - row3[0] * row1[1]
			val coef23 = row1[0] * row2[1] - row2[0] * row1[1]

			val fac0 = Vec4(coef00, coef00, coef02, coef03)
			val fac1 = Vec4(coef04, coef04, coef06, coef07)
			val fac2 = Vec4(coef08, coef08, coef10, coef11)
			val fac3 = Vec4(coef12, coef12, coef14, coef15)
			val fac4 = Vec4(coef16, coef16, coef18, coef19)
			val fac5 = Vec4(coef20, coef20, coef22, coef23)

			val vec0 = Vec4(row1[0], row0[0], row0[0], row0[0])
			val vec1 = Vec4(row1[1], row0[1], row0[1], row0[1])
			val vec2 = Vec4(row1[2], row0[2], row0[2], row0[2])
			val vec3 = Vec4(row1[3], row0[3], row0[3], row0[3])

			val inv0 = vec1 * fac0 - vec2 * fac1 + vec3 * fac2
			val inv1 = vec0 * fac0 - vec2 * fac3 + vec3 * fac4
			val inv2 = vec0 * fac1 - vec1 * fac3 + vec3 * fac5
			val inv3 = vec0 * fac2 - vec1 * fac4 + vec2 * fac5

			val signA = Vec4(+1f, -1f, +1f, -1f)
			val signB = Vec4(-1f, +1f, -1f, +1f)
			val inverse = Mat4(inv0 * signA, inv1 * signB, inv2 * signA, inv3 * signB)

			val row0 = Vec4(inverse[0][0], inverse[1][0], inverse[2][0], inverse[3][0])

			val dot0 = row0 * row0
			val dot1 = (dot0.x + dot0.y) + (dot0.z + dot0.w)

			val oneOverDeterminant = 1 / dot1

			return inverse * oneOverDeterminant
		}

	operator fun plus(scalar: Float): Mat4 = Mat4(
		row0 + scalar, row1 + scalar, row2 + scalar, row3 + scalar
	)

	operator fun minus(scalar: Float): Mat4 = Mat4(
		row0 - scalar, row1 - scalar, row2 - scalar, row3 - scalar
	)

	operator fun times(scalar: Float): Mat4 = Mat4(
		row0 * scalar, row1 * scalar, row2 * scalar, row3 * scalar
	)

	operator fun div(scalar: Float): Mat4 = Mat4(
		row0 / scalar, row1 / scalar, row2 / scalar, row3 / scalar
	)

	operator fun plus(other: Mat4): Mat4 = Mat4(
		row0 + other[0], row1 + other[1], row2 + other[2], row3 + other[3]
	)

	operator fun minus(other: Mat4): Mat4 = Mat4(
		row0 - other[0], row1 - other[1], row2 - other[2], row3 - other[3]
	)

	operator fun times(other: Mat4): Mat4 = Mat4(
		row0 * other[0], row1 * other[1], row2 * other[2], row3 * other[3]
	)

	operator fun div(other: Mat4): Mat4 = this * other.inverse


	operator fun times(vector: Vec4): Vec4 {
		val mov0 = Vec4(vector[0])
		val mov1 = Vec4(vector[1])
		val mul0 = row0 * mov0
		val mul1 = row1 * mov1
		val add0 = mul0 + mul1
		val mov2 = Vec4(vector[2])
		val mov3 = Vec4(vector[3])
		val mul2 = row2 * mov2
		val mul3 = row3 * mov3
		val add1 = mul2 + mul3
		return add0 + add1
	}

	fun transpose(): Mat4 = Mat4(
		row0[0], row1[0], row2[0], row3[0],
		row0[1], row1[1], row2[1], row3[1],
		row0[2], row1[2], row2[2], row3[2],
		row0[3], row1[3], row2[3], row3[3]
	)

	fun translate(vector: Vec3): Mat4 = copy(
		row3 = row0 * vector[0] + row1 * vector[1] + row2 * vector[2] + row3
	)

	fun rotate(angle: Float, v: Vec3): Mat4 {
		val a = angle
		val c = cos(a)
		val s = sin(a)

		val axis = v.normalize()
		val temp = (1 - c) * axis

		val rotate = Mat4().also {
			it[0][0] = c + temp[0] * axis[0]
			it[0][1] = temp[0] * axis[1] + s * axis[2]
			it[0][2] = temp[0] * axis[2] - s * axis[1]

			it[1][0] = temp[1] * axis[0] - s * axis[2]
			it[1][1] = c + temp[1] * axis[1]
			it[1][2] = temp[1] * axis[2] + s * axis[0]

			it[2][0] = temp[2] * axis[0] + s * axis[1]
			it[2][1] = temp[2] * axis[1] - s * axis[0]
			it[2][2] = c + temp[2] * axis[2]
		}

		return copy(
			row0 = row0 * rotate[0][0] + row1 * rotate[0][1] + row2 * rotate[0][2],
			row1 = row0 * rotate[1][0] + row1 * rotate[1][1] + row2 * rotate[1][2],
			row2 = row0 * rotate[2][0] + row1 * rotate[2][1] + row2 * rotate[2][2]
		)
	}

	fun scale(vector: Vec3): Mat4 = copy(
		row0 = row0 * vector[0],
		row1 = row1 * vector[1],
		row2 = row2 * vector[2]
	)


	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		return other is Mat4 &&
			row0 == other.row0 &&
			row1 == other.row1 &&
			row2 == other.row2 &&
			row3 == other.row3
	}

	override fun hashCode(): Int {
		var result = row0.hashCode()
		result = 31 * result + row1.hashCode()
		result = 31 * result + row2.hashCode()
		result = 31 * result + row3.hashCode()
		return result
	}

	override fun toString(): String = "${row0}\n${row1}\n${row2}\n${row3}"
}


fun Mat4.toFloatArray(): FloatArray = floatArrayOf(
	this[0][0], this[0][1], this[0][2], this[0][3],
	this[1][0], this[1][1], this[1][2], this[1][3],
	this[2][0], this[2][1], this[2][2], this[2][3],
	this[3][0], this[3][1], this[3][2], this[3][3]
)

fun Mat4.toMat4(): Mat4 = Mat4(
	this[0][0], this[0][1], this[0][2], this[0][3],
	this[1][0], this[1][1], this[1][2], this[1][3],
	this[2][0], this[2][1], this[2][2], this[2][3],
	this[3][0], this[3][1], this[3][2], this[3][3]
)


operator fun Float.plus(matrix: Mat4): Mat4 = Mat4(
	this + matrix[0], this + matrix[1], this + matrix[2], this + matrix[3]
)

operator fun Float.minus(matrix: Mat4): Mat4 = Mat4(
	this - matrix[0], this - matrix[1], this - matrix[2], this - matrix[3]
)

operator fun Float.times(matrix: Mat4): Mat4 = Mat4(
	this * matrix[0], this * matrix[1], this * matrix[2], this * matrix[3]
)

operator fun Float.div(matrix: Mat4): Mat4 = Mat4(
	this / matrix[0], this / matrix[1], this / matrix[2], this / matrix[3]
)
