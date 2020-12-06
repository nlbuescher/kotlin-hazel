package hazel.scene

import hazel.core.*
import kotlin.reflect.*

abstract class ScriptableEntity {
	internal var entity: Entity? = null

	inline fun <reified T : Any> getComponent() = getComponent(T::class)
	fun <T : Any> getComponent(type: KClass<T>): T = entity!!.getComponent(type)

	open fun onCreate() {}
	open fun onUpdate(timeStep: TimeStep) {}
	open fun onDestroy() {}
}
