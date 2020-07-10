package hazel

private fun mod(a: Float, b: Float): Float {
	val mod = a % b
	return if (mod >= 0) mod else mod + b
}

private fun differenceModulo(a: Float, b: Float, c: Float): Float {
	return mod(mod(a, c) - mod(b, c), c)
}

private fun getProgressionLastElement(start: Float, end: Float, step: Float): Float = when {
	step > 0 -> if (start >= end) end else end - differenceModulo(end, start, step)
	step < 0 -> if (start <= end) end else end + differenceModulo(start, end, step)
	else -> throw IllegalArgumentException("Step is zero.")
}

private class FloatProgressionIterator(first: Float, last: Float, val step: Float) : FloatIterator() {
	private val finalElement = last
	private var hasNext: Boolean = if (step > 0) first <= last else first >= last
	private var next = if (hasNext) first else finalElement

	override fun hasNext(): Boolean = hasNext

	override fun nextFloat(): Float {
		val value = next
		if (value == finalElement) {
			if (!hasNext) throw NoSuchElementException()
			hasNext = false
		} else {
			next += step
		}
		return value
	}
}

open class FloatProgression(
	start: Float,
	endInclusive: Float,
	val step: Float
) : Iterable<Float> {
	init {
		if (step == 0f) throw IllegalArgumentException("Step must be non-zero.")
		if (step.isNaN()) throw IllegalArgumentException("Step must be a number.")
		if (step.isInfinite()) throw IllegalArgumentException("Step must be finite.")
	}

	val first: Float = start
	val last: Float = getProgressionLastElement(start, endInclusive, step)

	override fun iterator(): FloatIterator = FloatProgressionIterator(first, last, step)

	open fun isEmpty(): Boolean = if (step > 0) first > last else first < last

	override fun equals(other: Any?): Boolean =
		other is FloatProgression &&
			(isEmpty() && other.isEmpty() ||
				first == other.first && last == other.last && step == other.step)

	override fun hashCode(): Int =
		if (isEmpty()) -1 else (31 * (31 * first.hashCode() + last.hashCode()) + step.hashCode())

	override fun toString(): String = if (step > 0) "$first..$last step $step" else "$first downTo $last step ${-step}"

	companion object {
		fun fromClosedRange(rangeStart: Float, rangeEnd: Float, step: Float): FloatProgression =
			FloatProgression(rangeStart, rangeEnd, step)
	}
}

class FloatRange(start: Float, endInclusive: Float) : FloatProgression(start, endInclusive, 1f),
	ClosedFloatingPointRange<Float> {
	override val start: Float get() = first
	override val endInclusive: Float get() = last

	override fun lessThanOrEquals(a: Float, b: Float): Boolean = a <= b

	override fun isEmpty(): Boolean = first > last

	override fun equals(other: Any?): Boolean =
		other is FloatRange && (isEmpty() && other.isEmpty() ||
			first == other.first && last == other.last)

	override fun hashCode(): Int =
		if (isEmpty()) -1 else (31 * first.hashCode() + last.hashCode())

	override fun toString(): String = "$first..$last"

	companion object {
		val EMPTY: FloatRange = FloatRange(1f, 0f)
	}
}

operator fun Float.rangeTo(other: Float): FloatProgression = FloatRange(this, other)

infix fun Float.downTo(other: Float): FloatProgression = FloatProgression.fromClosedRange(this, other, -1f)

infix fun FloatProgression.step(step: Float): FloatProgression {
	if (step < 0) throw IllegalArgumentException("Step must be positive, was: $step.")
	return FloatProgression.fromClosedRange(first, last, if (this.step > 0) step else -step)
}
