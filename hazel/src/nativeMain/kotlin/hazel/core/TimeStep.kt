package hazel.core

enum class TimeStepUnit(internal val scale: Float) {
	MILLISECONDS(1e-3f),
	SECONDS(1e0f)
}

private fun convertTimeStepUnit(value: Float, sourceUnit: TimeStepUnit, targetUnit: TimeStepUnit): Float {
	val sourceTargetCompare = sourceUnit.scale.compareTo(targetUnit.scale)
	return when {
		sourceTargetCompare > 0 -> value * (sourceUnit.scale / targetUnit.scale)
		sourceTargetCompare < 0 -> value / (targetUnit.scale / sourceUnit.scale)
		else -> value
	}
}

private inline val storageUnit get() = TimeStepUnit.MILLISECONDS

@Suppress("NON_PUBLIC_PRIMARY_CONSTRUCTOR_OF_INLINE_CLASS")
inline class TimeStep internal constructor(internal val value: Float) {
	companion object {
		val ZERO = TimeStep(0f)

		fun convert(value: Float, sourceUnit: TimeStepUnit, targetUnit: TimeStepUnit) =
			convertTimeStepUnit(value, sourceUnit, targetUnit)
	}

	fun toFloat(unit: TimeStepUnit) = convertTimeStepUnit(value, storageUnit, unit)

	val inSeconds: Float get() = toFloat(TimeStepUnit.SECONDS)
	val inMilliseconds: Float get() = toFloat(TimeStepUnit.MILLISECONDS)
}

fun Float.toTimeStep(unit: TimeStepUnit) = TimeStep(convertTimeStepUnit(this, unit, storageUnit))
