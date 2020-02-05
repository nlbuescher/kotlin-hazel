package hazel.math

import kotlin.math.cos
import kotlin.math.sin

sealed class Matrix4x4<T : Number> {
	abstract operator fun get(row: Int): Vector4<T>
	abstract operator fun set(row: Int, value: Vector4<T>)

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Matrix4x4<*>) return false

		if (this[0] != other[0]) return false
		if (this[1] != other[1]) return false
		if (this[2] != other[2]) return false
		if (this[3] != other[3]) return false

		return true
	}

	override fun hashCode(): Int {
		var result = this[0].hashCode()
		result += 31 * this[1].hashCode()
		result += 31 * this[2].hashCode()
		result += 31 * this[3].hashCode()
		return result
	}

	override fun toString() = "${this[0]}\n${this[1]}\n${this[2]}\n${this[3]}"

	abstract fun copy(): Matrix4x4<T>

	// matrix

	abstract fun inv(): Matrix4x4<T>
	abstract fun translate(vector: Vector3<T>): Matrix4x4<T>
	abstract fun rotate(angle: Float, vector: Vector3<T>): Matrix4x4<T>
	abstract fun scale(vector: Vector3<T>): Matrix4x4<T>

	// arithmetic

	abstract operator fun plus(scalar: T): Matrix4x4<T>
	abstract operator fun minus(scalar: T): Matrix4x4<T>
	abstract operator fun times(scalar: T): Matrix4x4<T>
	abstract operator fun div(scalar: T): Matrix4x4<T>

	abstract operator fun plusAssign(scalar: T)
	abstract operator fun minusAssign(scalar: T)
	abstract operator fun timesAssign(scalar: T)
	abstract operator fun divAssign(scalar: T)

	abstract operator fun plus(other: Matrix4x4<T>): Matrix4x4<T>
	abstract operator fun minus(other: Matrix4x4<T>): Matrix4x4<T>
	abstract operator fun times(other: Matrix4x4<T>): Matrix4x4<T>
	abstract operator fun div(other: Matrix4x4<T>): Matrix4x4<T>

	abstract operator fun plusAssign(other: Matrix4x4<T>)
	abstract operator fun minusAssign(other: Matrix4x4<T>)
	abstract operator fun timesAssign(other: Matrix4x4<T>)
	abstract operator fun divAssign(other: Matrix4x4<T>)
}

class FloatMatrix4x4(
	private var row0: FloatVector4,
	private var row1: FloatVector4,
	private var row2: FloatVector4,
	private var row3: FloatVector4
) : Matrix4x4<Float>() {
	constructor(
		r0c0: Float, r0c1: Float, r0c2: Float, r0c3: Float,
		r1c0: Float, r1c1: Float, r1c2: Float, r1c3: Float,
		r2c0: Float, r2c1: Float, r2c2: Float, r2c3: Float,
		r3c0: Float, r3c1: Float, r3c2: Float, r3c3: Float
	) : this(
		FloatVector4(r0c0, r0c1, r0c2, r0c3),
		FloatVector4(r1c0, r1c1, r1c2, r1c3),
		FloatVector4(r2c0, r2c1, r2c2, r2c3),
		FloatVector4(r3c0, r3c1, r3c2, r3c3)
	)

	constructor(scalar: Float) : this(
		scalar, 0f, 0f, 0f,
		0f, scalar, 0f, 0f,
		0f, 0f, scalar, 0f,
		0f, 0f, 0f, scalar
	)

	constructor() : this(1f)

	override fun get(row: Int) = when (row) {
		0 -> row0; 1 -> row1; 2 -> row2; 3 -> row3
		else -> throw IndexOutOfBoundsException()
	}

	override fun set(row: Int, value: Vector4<Float>) = when (row) {
		0 -> row0 = value as FloatVector4; 1 -> row1 = value as FloatVector4; 2 -> row2 = value as FloatVector4; 3 -> row3 = value as FloatVector4
		else -> throw IndexOutOfBoundsException()
	}

	override fun copy() = FloatMatrix4x4(this[0].copy(), this[1].copy(), this[2].copy(), this[3].copy())

	fun toFloatArray() = floatArrayOf(*this[0].asFloatArray(), *this[1].asFloatArray(), *this[2].asFloatArray(), *this[3].asFloatArray())

	// matrix

	override fun inv(): FloatMatrix4x4 {
		val c00 = this[2][2] * this[3][3] - this[3][2] * this[2][3]
		val c02 = this[1][2] * this[3][3] - this[3][2] * this[1][3]
		val c03 = this[1][2] * this[2][3] - this[2][2] * this[1][3]

		val c04 = this[2][1] * this[3][3] - this[3][1] * this[2][3]
		val c06 = this[1][1] * this[3][3] - this[3][1] * this[1][3]
		val c07 = this[1][1] * this[2][3] - this[2][1] * this[1][3]

		val c08 = this[2][1] * this[3][2] - this[3][1] * this[2][2]
		val c10 = this[1][1] * this[3][2] - this[3][1] * this[1][2]
		val c11 = this[1][1] * this[2][2] - this[2][1] * this[1][2]

		val c12 = this[2][0] * this[3][3] - this[3][0] * this[2][3]
		val c14 = this[1][0] * this[3][3] - this[3][0] * this[1][3]
		val c15 = this[1][0] * this[2][3] - this[2][0] * this[1][3]

		val c16 = this[2][0] * this[3][2] - this[3][0] * this[2][2]
		val c18 = this[1][0] * this[3][2] - this[3][0] * this[1][2]
		val c19 = this[1][0] * this[2][2] - this[2][0] * this[1][2]

		val c20 = this[2][0] * this[3][1] - this[3][0] * this[2][1]
		val c22 = this[1][0] * this[3][1] - this[3][0] * this[1][1]
		val c23 = this[1][0] * this[2][1] - this[2][0] * this[1][1]

		val f0 = FloatVector4(c00, c00, c02, c03)
		val f1 = FloatVector4(c04, c04, c06, c07)
		val f2 = FloatVector4(c08, c08, c10, c11)
		val f3 = FloatVector4(c12, c12, c14, c15)
		val f4 = FloatVector4(c16, c16, c18, c19)
		val f5 = FloatVector4(c20, c20, c22, c23)

		val v0 = FloatVector4(this[1][0], this[0][0], this[0][0], this[0][0])
		val v1 = FloatVector4(this[1][1], this[0][1], this[0][1], this[0][1])
		val v2 = FloatVector4(this[1][2], this[0][2], this[0][2], this[0][2])
		val v3 = FloatVector4(this[1][3], this[0][3], this[0][3], this[0][3])

		val i0 = v1 * f0 - v2 * f1 + v3 * f2
		val i1 = v0 * f0 - v2 * f3 + v3 * f4
		val i2 = v0 * f1 - v1 * f3 + v3 * f5
		val i3 = v0 * f2 - v1 * f4 + v2 * f5

		val signA = FloatVector4(+1f, -1f, +1f, -1f)
		val signB = FloatVector4(-1f, +1f, -1f, +1f)
		val inverse = FloatMatrix4x4(i0 * signA, i1 * signB, i2 * signA, i3 * signB)

		val row0 = FloatVector4(inverse[0][0], inverse[1][0], inverse[2][0], inverse[3][0])

		val dot0 = this[0] * row0
		val dot1 = (dot0.x + dot0.y) + (dot0.z + dot0.w)

		val oneOverDeterminant = 1f / dot1

		return inverse * oneOverDeterminant
	}


	override fun plus(scalar: Float) = FloatMatrix4x4(this[0] + scalar, this[1] + scalar, this[2] + scalar, this[3] + scalar)
	override fun minus(scalar: Float) = FloatMatrix4x4(this[0] - scalar, this[1] - scalar, this[2] - scalar, this[3] - scalar)
	override fun times(scalar: Float) = FloatMatrix4x4(this[0] * scalar, this[1] * scalar, this[2] * scalar, this[3] * scalar)
	override fun div(scalar: Float) = FloatMatrix4x4(this[0] / scalar, this[1] / scalar, this[2] / scalar, this[3] / scalar)

	override fun plusAssign(scalar: Float) = run { this[0].plusAssign(scalar); this[1].plusAssign(scalar); this[2].plusAssign(scalar); this[3].plusAssign(scalar) }
	override fun minusAssign(scalar: Float) = run { this[0].minusAssign(scalar); this[1].minusAssign(scalar); this[2].minusAssign(scalar); this[3].minusAssign(scalar) }
	override fun timesAssign(scalar: Float) = run { this[0].timesAssign(scalar); this[1].timesAssign(scalar); this[2].timesAssign(scalar); this[3].timesAssign(scalar) }
	override fun divAssign(scalar: Float) = run { this[0].divAssign(scalar); this[1].divAssign(scalar); this[2].divAssign(scalar); this[3].divAssign(scalar) }


	override fun plus(other: Matrix4x4<Float>) = FloatMatrix4x4(this[0] + other[0], this[1] + other[1], this[2] + other[2], this[3] + other[3])
	override fun minus(other: Matrix4x4<Float>) = FloatMatrix4x4(this[0] - other[0], this[1] - other[1], this[2] - other[2], this[3] - other[3])
	override fun times(other: Matrix4x4<Float>) = FloatMatrix4x4(
		this[0] * other[0][0] + this[1] * other[0][1] + this[2] * other[0][2] + this[3] * other[0][3],
		this[0] * other[1][0] + this[1] * other[1][1] + this[2] * other[1][2] + this[3] * other[1][3],
		this[0] * other[2][0] + this[1] * other[2][1] + this[2] * other[2][2] + this[3] * other[2][3],
		this[0] * other[3][0] + this[1] * other[3][1] + this[2] * other[3][2] + this[3] * other[3][3]
	)

	override fun div(other: Matrix4x4<Float>) = this * other.inv()


	override fun plusAssign(other: Matrix4x4<Float>) = run { this[0].plusAssign(other[0]); this[1].plusAssign(other[1]); this[2].plusAssign(other[2]); this[3].plusAssign(other[3]) }
	override fun minusAssign(other: Matrix4x4<Float>) = run { this[0].minusAssign(other[0]); this[1].minusAssign(other[1]); this[2].minusAssign(other[2]); this[3].minusAssign(other[3]) }
	override fun timesAssign(other: Matrix4x4<Float>) {
		val temp00 = this[0][0] * other[0][0] + this[1][0] * other[0][1] + this[2][0] * other[0][2] + this[3][0] * other[0][3]
		val temp01 = this[0][1] * other[0][0] + this[1][1] * other[0][1] + this[2][1] * other[0][2] + this[3][1] * other[0][3]
		val temp02 = this[0][2] * other[0][0] + this[1][2] * other[0][1] + this[2][2] * other[0][2] + this[3][2] * other[0][3]
		val temp03 = this[0][3] * other[0][0] + this[1][3] * other[0][1] + this[2][3] * other[0][2] + this[3][3] * other[0][3]

		val temp10 = this[0][0] * other[1][0] + this[1][0] * other[1][1] + this[2][0] * other[1][2] + this[3][0] * other[1][3]
		val temp11 = this[0][1] * other[1][0] + this[1][1] * other[1][1] + this[2][1] * other[1][2] + this[3][1] * other[1][3]
		val temp12 = this[0][2] * other[1][0] + this[1][2] * other[1][1] + this[2][2] * other[1][2] + this[3][2] * other[1][3]
		val temp13 = this[0][3] * other[1][0] + this[1][3] * other[1][1] + this[2][3] * other[1][2] + this[3][3] * other[1][3]

		val temp20 = this[0][0] * other[2][0] + this[1][0] * other[2][1] + this[2][0] * other[2][2] + this[3][0] * other[2][3]
		val temp21 = this[0][1] * other[2][0] + this[1][1] * other[2][1] + this[2][1] * other[2][2] + this[3][1] * other[2][3]
		val temp22 = this[0][2] * other[2][0] + this[1][2] * other[2][1] + this[2][2] * other[2][2] + this[3][2] * other[2][3]
		val temp23 = this[0][3] * other[2][0] + this[1][3] * other[2][1] + this[2][3] * other[2][2] + this[3][3] * other[2][3]

		val temp30 = this[0][0] * other[3][0] + this[1][0] * other[3][1] + this[2][0] * other[3][2] + this[3][0] * other[3][3]
		val temp31 = this[0][1] * other[3][0] + this[1][1] * other[3][1] + this[2][1] * other[3][2] + this[3][1] * other[3][3]
		val temp32 = this[0][2] * other[3][0] + this[1][2] * other[3][1] + this[2][2] * other[3][2] + this[3][2] * other[3][3]
		val temp33 = this[0][3] * other[3][0] + this[1][3] * other[3][1] + this[2][3] * other[3][2] + this[3][3] * other[3][3]

		this[0][0] = temp00; this[0][1] = temp01; this[0][2] = temp02; this[0][3] = temp03
		this[1][0] = temp10; this[1][1] = temp11; this[1][2] = temp12; this[1][3] = temp13
		this[2][0] = temp20; this[2][1] = temp21; this[2][2] = temp22; this[2][3] = temp23
		this[3][0] = temp30; this[3][1] = temp31; this[3][2] = temp32; this[3][3] = temp33
	}

	override fun divAssign(other: Matrix4x4<Float>) = this.timesAssign(other.inv())


	override fun translate(vector: Vector3<Float>) = copy().also {
		it[3] = this[0] * vector[0] + this[1] * vector[1] + this[2] * vector[2] + this[3]
	}

	override fun rotate(angle: Float, vector: Vector3<Float>): FloatMatrix4x4 {
		val cosine = cos(angle)
		val sine = sin(angle)

		val axis = vector.normalize()
		val temp = axis * (1 - cosine)

		val rotate = FloatMatrix4x4().also {
			it[0][0] = cosine + temp[0] * axis[0]
			it[0][1] = temp[0] * axis[1] + sine * axis[2]
			it[0][2] = temp[0] * axis[2] - sine * axis[1]

			it[1][0] = temp[1] * axis[0] - sine * axis[2]
			it[1][1] = cosine + temp[1] * axis[1]
			it[1][2] = temp[1] * axis[2] + sine * axis[0]

			it[2][0] = temp[2] * axis[0] + sine * axis[1]
			it[2][1] = temp[2] * axis[1] - sine * axis[0]
			it[2][2] = cosine + temp[2] * axis[2]
		}

		return FloatMatrix4x4(
			this[0] * rotate[0][0] + this[1] * rotate[0][1] + this[2] * rotate[0][2],
			this[0] * rotate[1][0] + this[1] * rotate[1][1] + this[2] * rotate[1][2],
			this[0] * rotate[2][0] + this[1] * rotate[2][1] + this[2] * rotate[2][2],
			this[3]
		)
	}

	override fun scale(vector: Vector3<Float>) = FloatMatrix4x4(this[0] * vector[0], this[1] * vector[1], this[2] * vector[2], this[3])
}
