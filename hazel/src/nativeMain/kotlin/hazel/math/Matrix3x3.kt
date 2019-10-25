package hazel.math

sealed class Matrix3x3<T : Number> {
    abstract operator fun get(row: Int): Vector3<T>
    abstract operator fun set(row: Int, value: Vector3<T>)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Matrix3x3<*>) return false

        if (this[0] != other[0]) return false
        if (this[1] != other[1]) return false
        if (this[2] != other[2]) return false

        return true
    }

    override fun hashCode(): Int {
        var result = this[0].hashCode()
        result += 31 * this[1].hashCode()
        result += 31 * this[2].hashCode()
        return result
    }

    override fun toString() = "${this[0]}\n${this[1]}\n${this[2]}"

    abstract fun copy(): Matrix3x3<T>

    // matrix

    abstract fun inv(): Matrix3x3<T>

    // arithmetic

    abstract operator fun plus(scalar: T): Matrix3x3<T>
    abstract operator fun minus(scalar: T): Matrix3x3<T>
    abstract operator fun times(scalar: T): Matrix3x3<T>
    abstract operator fun div(scalar: T): Matrix3x3<T>

    abstract operator fun plusAssign(scalar: T)
    abstract operator fun minusAssign(scalar: T)
    abstract operator fun timesAssign(scalar: T)
    abstract operator fun divAssign(scalar: T)

    abstract operator fun plus(other: Matrix3x3<T>): Matrix3x3<T>
    abstract operator fun minus(other: Matrix3x3<T>): Matrix3x3<T>
    abstract operator fun times(other: Matrix3x3<T>): Matrix3x3<T>
    abstract operator fun div(other: Matrix3x3<T>): Matrix3x3<T>

    abstract operator fun plusAssign(other: Matrix3x3<T>)
    abstract operator fun minusAssign(other: Matrix3x3<T>)
    abstract operator fun timesAssign(other: Matrix3x3<T>)
    abstract operator fun divAssign(other: Matrix3x3<T>)
}

class FloatMatrix3x3(
    private var row0: FloatVector3,
    private var row1: FloatVector3,
    private var row2: FloatVector3
) : Matrix3x3<Float>() {
    constructor(
        r0c0: Float, r0c1: Float, r0c2: Float,
        r1c0: Float, r1c1: Float, r1c2: Float,
        r2c0: Float, r2c1: Float, r2c2: Float
    ) : this(
        FloatVector3(r0c0, r0c1, r0c2),
        FloatVector3(r1c0, r1c1, r1c2),
        FloatVector3(r2c0, r2c1, r2c2)
    )

    constructor(scalar: Float) : this(
        scalar, 0f, 0f,
        0f, scalar, 0f,
        0f, 0f, scalar
    )

    constructor() : this(1f)

    override fun get(row: Int) = when (row) {
        0 -> row0; 1 -> row1; 2 -> row2
        else -> throw IndexOutOfBoundsException()
    }

    override fun set(row: Int, value: Vector3<Float>) = when (row) {
        0 -> row0 = value as FloatVector3; 1 -> row1 = value as FloatVector3; 2 -> row2 = value as FloatVector3
        else -> throw IndexOutOfBoundsException()
    }

    override fun copy() = FloatMatrix3x3(this[0].copy(), this[1].copy(), this[2].copy())

    fun toFloatArray() = floatArrayOf(*this[0].asFloatArray(), *this[1].asFloatArray(), *this[2].asFloatArray())

    // matrix

    override fun inv(): FloatMatrix3x3 {
        val oneOverDeterminant = 1f / (
            +this[0][0] * (this[1][1] * this[2][2] - this[2][1] * this[1][2])
                - this[1][0] * (this[0][1] * this[2][2] - this[2][1] * this[0][2])
                + this[2][0] * (this[0][1] * this[1][2] - this[1][1] * this[0][2]))

        return FloatMatrix3x3(
            +(this[1][1] * this[2][2] - this[2][1] * this[1][2]) * oneOverDeterminant,
            -(this[1][0] * this[2][2] - this[2][0] * this[1][2]) * oneOverDeterminant,
            +(this[1][0] * this[2][1] - this[2][0] * this[1][1]) * oneOverDeterminant,
            -(this[0][1] * this[2][2] - this[2][1] * this[0][2]) * oneOverDeterminant,
            +(this[0][0] * this[2][2] - this[2][0] * this[0][2]) * oneOverDeterminant,
            -(this[0][0] * this[2][1] - this[2][0] * this[0][1]) * oneOverDeterminant,
            +(this[0][1] * this[1][2] - this[1][1] * this[0][2]) * oneOverDeterminant,
            -(this[0][0] * this[1][2] - this[1][0] * this[0][2]) * oneOverDeterminant,
            +(this[0][0] * this[1][1] - this[1][0] * this[0][1]) * oneOverDeterminant
        )
    }


    override fun plus(scalar: Float) = FloatMatrix3x3(this[0] + scalar, this[1] + scalar, this[2] + scalar)
    override fun minus(scalar: Float) = FloatMatrix3x3(this[0] - scalar, this[1] - scalar, this[2] - scalar)
    override fun times(scalar: Float) = FloatMatrix3x3(this[0] * scalar, this[1] * scalar, this[2] * scalar)
    override fun div(scalar: Float) = FloatMatrix3x3(this[0] / scalar, this[1] / scalar, this[2] / scalar)

    override fun plusAssign(scalar: Float) = run { this[0].plusAssign(scalar); this[1].plusAssign(scalar); this[2].plusAssign(scalar) }
    override fun minusAssign(scalar: Float) = run { this[0].minusAssign(scalar); this[1].minusAssign(scalar); this[2].minusAssign(scalar) }
    override fun timesAssign(scalar: Float) = run { this[0].timesAssign(scalar); this[1].timesAssign(scalar); this[2].timesAssign(scalar) }
    override fun divAssign(scalar: Float) = run { this[0].divAssign(scalar); this[1].divAssign(scalar); this[2].divAssign(scalar) }


    override fun plus(other: Matrix3x3<Float>) = FloatMatrix3x3(this[0] + other[0], this[1] + other[1], this[2] + other[2])
    override fun minus(other: Matrix3x3<Float>) = FloatMatrix3x3(this[0] - other[0], this[1] - other[1], this[2] - other[2])
    override fun times(other: Matrix3x3<Float>) = FloatMatrix3x3(
        this[0] * other[0][0] + this[1] * other[0][1] + this[2] * other[0][2],
        this[0] * other[1][0] + this[1] * other[1][1] + this[2] * other[1][2],
        this[0] * other[2][0] + this[1] * other[2][1] + this[2] * other[2][2]
    )

    override fun div(other: Matrix3x3<Float>) = this * other.inv()


    override fun plusAssign(other: Matrix3x3<Float>) = run { this[0].plusAssign(other[0]); this[1].plusAssign(other[1]); this[2].plusAssign(other[2]) }
    override fun minusAssign(other: Matrix3x3<Float>) = run { this[0].minusAssign(other[0]); this[1].minusAssign(other[1]); this[2].minusAssign(other[2]) }
    override fun timesAssign(other: Matrix3x3<Float>) {
        val temp00 = this[0][0] * other[0][0] + this[1][0] * other[0][1] + this[2][0] * other[0][2]
        val temp01 = this[0][1] * other[0][0] + this[1][1] * other[0][1] + this[2][1] * other[0][2]
        val temp02 = this[0][2] * other[0][0] + this[1][2] * other[0][1] + this[2][2] * other[0][2]

        val temp10 = this[0][0] * other[1][0] + this[1][0] * other[1][1] + this[2][0] * other[1][2]
        val temp11 = this[0][1] * other[1][0] + this[1][1] * other[1][1] + this[2][1] * other[1][2]
        val temp12 = this[0][2] * other[1][0] + this[1][2] * other[1][1] + this[2][2] * other[1][2]

        val temp20 = this[0][0] * other[2][0] + this[1][0] * other[2][1] + this[2][0] * other[2][2]
        val temp21 = this[0][1] * other[2][0] + this[1][1] * other[2][1] + this[2][1] * other[2][2]
        val temp22 = this[0][2] * other[2][0] + this[1][2] * other[2][1] + this[2][2] * other[2][2]

        this[0][0] = temp00; this[0][1] = temp01; this[0][2] = temp02
        this[1][0] = temp10; this[1][1] = temp11; this[1][2] = temp12
        this[2][0] = temp20; this[2][1] = temp21; this[2][2] = temp22
    }

    override fun divAssign(other: Matrix3x3<Float>) = this.timesAssign(other.inv())
}
