package hazel.ecs

import kotlin.reflect.*

internal const val ENTITY_MASK = 0xFFFFFu
internal const val VERSION_MASK = 0xFFFu
internal const val ENTITY_SHIFT = 20

inline class EntityId(val value: UInt) {
	companion object {
		val Null = EntityId(ENTITY_MASK)
	}
}

inline class TypeId(val value: UInt)

data class TypeInfo(val id: TypeId, val index: Int)

private var typeIndexValue: Int = 0
private val typeIndexMap = mutableMapOf<KClass<*>, Int>()
private inline val KClass<*>.typeIndex: Int
	get() = typeIndexMap[this] ?: typeIndexValue.let { index ->
		typeIndexValue += 1
		typeIndexMap[this] = index
		index
	}

val KClass<*>.typeInfo get() = TypeInfo(TypeId(hashCode().toUInt()), typeIndex)
