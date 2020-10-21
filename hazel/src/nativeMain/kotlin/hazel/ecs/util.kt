package hazel.ecs

import kotlin.reflect.*

inline class EntityId(val value: UInt)

data class TypeInfo(val id: UInt, val index: Int)

private var typeIndexValue: Int = 0
private val typeIndexMap = mutableMapOf<KClass<*>, Int>()
private inline val KClass<*>.typeIndex: Int
	get() = typeIndexMap[this] ?: typeIndexValue.let { index ->
		typeIndexValue += 1
		typeIndexMap[this] = index
		index
	}

val KClass<*>.typeInfo get() = TypeInfo(hashCode().toUInt(), typeIndex)