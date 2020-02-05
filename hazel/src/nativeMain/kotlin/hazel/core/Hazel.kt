@file:Suppress("unused")

package hazel.core

import hazel.debug.Instrumentor
import hazel.debug.ProfileResult
import kotlin.native.concurrent.ensureNeverFrozen
import kotlin.native.internal.KFunctionImpl
import kotlin.reflect.KFunction
import kotlin.time.MonoClock

private var _config = Hazel.Config()

private var _application: Application? = null
	set(value) {
		if (field != null) error("application already set!")
		field = value
	}

private val coreLogger = Logger("HAZEL")
private val clientLogger = Logger("APP")

private val clock = MonoClock.markNow()

object Hazel {
	class Config {
		var isProfileEnabled: Boolean = Platform.isDebugBinary
		var isAssertsEnabled: Boolean = Platform.isDebugBinary
	}

	class Profiler(private val name: String) {

		constructor(function: KFunction<*>) : this((function as KFunctionImpl).fqName)

		init {
			ensureNeverFrozen()
		}

		private var start: Long = 0
		private var end: Long = 0

		fun start() {
			if (config.isProfileEnabled) {
				start = clock.elapsedNow().inMicroseconds.toLong()
			}
		}

		fun stop() {
			if (config.isProfileEnabled) {
				end = clock.elapsedNow().inMicroseconds.toLong()
				Instrumentor.writeProfile(ProfileResult(name, start, end, hazel.system.getThreadId()))
			}
		}
	}

	val config: Config get() = _config

	val application: Application get() = _application ?: error("must call Hazel.run first!")

	fun run(createApplication: () -> Application) {
		Instrumentor.session("Startup", "startup_profile.json") {
			_application = createApplication()
		}

		coreWarn { "Initialized Log!" }
		info { "Hello!" }

		Instrumentor.session("Runtime", "runtime_profile.json") {
			application.run()
		}

		Instrumentor.session("Shutdown", "shutdown_profile.json") {
			application.dispose()
		}
	}
}

/** get current time in seconds since application start */
fun Hazel.getTime(): Float = clock.elapsedNow().inSeconds.toFloat()

internal fun Hazel.coreTrace(message: () -> Any?) = coreLogger.trace(message().toString())
internal fun Hazel.coreDebug(message: () -> Any?) = coreLogger.debug(message().toString())
internal fun Hazel.coreInfo(message: () -> Any?) = coreLogger.info(message().toString())
internal fun Hazel.coreWarn(message: () -> Any?) = coreLogger.warn(message().toString())
internal fun Hazel.coreError(message: () -> Any?) = coreLogger.error(message().toString())
internal fun Hazel.coreCritical(message: () -> Any?) = coreLogger.critical(message().toString())
internal fun Hazel.coreAssert(test: Boolean, message: () -> Any? = { null }) {
	if (config.isAssertsEnabled && !test) {
		coreCritical { "Assertion failed${message()?.let { ": $it" } ?: ""}" }
		hazel.system.breakpoint()
	}
}

fun Hazel.trace(message: () -> Any?) = clientLogger.trace(message().toString())
fun Hazel.debug(message: () -> Any?) = clientLogger.debug(message().toString())
fun Hazel.info(message: () -> Any?) = clientLogger.info(message().toString())
fun Hazel.warn(message: () -> Any?) = clientLogger.warn(message().toString())
fun Hazel.error(message: () -> Any?) = clientLogger.error(message().toString())
fun Hazel.critical(message: () -> Any?) = clientLogger.critical(message().toString())
fun Hazel.assert(test: Boolean, message: () -> Any? = { null }) {
	if (config.isAssertsEnabled && !test) {
		critical { "Assertion failed${message()?.let { ": $it" } ?: ""}" }
		hazel.system.breakpoint()
	}
}

fun <R> Hazel.profile(name: String, block: () -> R): R {
	val profiler = Hazel.Profiler(name)
	profiler.start()
	val ret = block()
	profiler.stop()
	return ret
}

fun <R> Hazel.profile(function: KFunction<R>, block: () -> R): R = profile((function as KFunctionImpl).fqName, block)
