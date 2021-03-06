@file:Suppress("unused")

package hazel.core

import hazel.debug.*
import hazel.system.*
import kotlin.native.ThreadLocal
import kotlin.native.concurrent.*
import kotlin.time.*

@Suppress("MemberVisibilityCanBePrivate")
@ThreadLocal
object Hazel {
	@ThreadLocal
	object Config {
		var enableProfiling: Boolean = Platform.isDebugBinary
		var enableAsserts: Boolean = Platform.isDebugBinary
	}


	private val clock = TimeSource.Monotonic.markNow()

	/** get current time in seconds since application start */
	val currentTime: Float get() = clock.elapsedNow().inSeconds.toFloat()


	private val coreLogger = Logger("HAZEL")
	private val clientLogger = Logger("APP")

	internal fun coreTrace(message: String) = coreLogger.trace(message)
	internal fun coreDebug(message: String) = coreLogger.debug(message)
	internal fun coreInfo(message: String) = coreLogger.info(message)
	internal fun coreWarn(message: String) = coreLogger.warn(message)
	internal fun coreError(message: String) = coreLogger.error(message)
	internal fun coreCritical(message: String) = coreLogger.critical(message)
	internal fun coreAssert(test: Boolean, message: String? = null) {
		if (Config.enableAsserts && !test) {
			coreCritical("Assertion failed${message?.let { ": $it" } ?: ""}")
			breakpoint()
		}
	}

	fun trace(message: String) = clientLogger.trace(message)
	fun debug(message: String) = clientLogger.debug(message)
	fun info(message: String) = clientLogger.info(message)
	fun warn(message: String) = clientLogger.warn(message)
	fun error(message: String) = clientLogger.error(message)
	fun critical(message: String) = clientLogger.critical(message)
	fun assert(test: Boolean, message: String? = null) {
		if (Config.enableAsserts && !test) {
			critical("Assertion failed${message?.let { ": $it" } ?: ""}")
			breakpoint()
		}
	}


	@Suppress("ObjectPropertyName")
	private var _application: Application? = null
		set(value) {
			if (field != null) kotlin.error("application already set!")
			field = value
		}
	val application: Application
		get() = _application ?: kotlin.error("must call Hazel.run first!")


	fun run(createApplication: () -> Application) {
		Instrumentor.session("Startup", "startup_profile.json") {
			_application = createApplication()
		}

		coreWarn("Initialized Log!")
		info("Hello!")

		Instrumentor.session("Runtime", "runtime_profile.json") {
			application.run()
		}

		Instrumentor.session("Shutdown", "shutdown_profile.json") {
			application.dispose()
		}
	}


	class Profiler(private val name: String) {
		init {
			ensureNeverFrozen()
		}

		private var start: Long = 0
		private var end: Long = 0

		fun start() {
			if (Config.enableProfiling) {
				start = clock.elapsedNow().inMicroseconds.toLong()
			}
		}

		fun stop() {
			if (Config.enableProfiling) {
				end = clock.elapsedNow().inMicroseconds.toLong()
				Instrumentor.writeProfile(ProfileResult(name, start, end, getThreadId()))
			}
		}
	}
}

//@OptIn(ExperimentalContracts::class)
inline fun <R> Hazel.profile(name: String, block: () -> R): R {
	//contract {
	//	callsInPlace(block, InvocationKind.EXACTLY_ONCE)
	//}
	val profiler = Hazel.Profiler(name)
	profiler.start()
	val ret = block()
	profiler.stop()
	return ret
}
