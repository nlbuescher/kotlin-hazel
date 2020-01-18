package hazel.core

import kotlin.time.MonoClock

private var _application: Application? = null
    set(value) {
        if (field != null) error("application already set!")
        field = value
    }

object Hazel {
    private val coreLogger = Logger("HAZEL")
    private val clientLogger = Logger("APP")

    private val start = MonoClock.markNow()

    val application: Application get() = _application ?: error("must instantiate an Application first!")

    /** current time in seconds since application start */
    val time: Float get() = start.elapsedNow().inSeconds.toFloat()


    fun run(application: Application) {
        _application = application

        coreWarn { "Initialized Log!" }
        info { "Hello!" }

        application.use { it.run() }
    }


    internal fun coreTrace(message: () -> Any?) = coreLogger.trace(message().toString())
    internal fun coreDebug(message: () -> Any?) = coreLogger.debug(message().toString())
    internal fun coreInfo(message: () -> Any?) = coreLogger.info(message().toString())
    internal fun coreWarn(message: () -> Any?) = coreLogger.warn(message().toString())
    internal fun coreError(message: () -> Any?) = coreLogger.error(message().toString())
    internal fun coreCritical(message: () -> Any?) = coreLogger.critical(message().toString())
    internal fun coreAssert(test: Boolean, message: () -> Any? = { null }) {
        if (Platform.isDebugBinary && !test) {
            coreCritical { "Assertion failed${message()?.let { ": $it" } ?: ""}" }
            //raise(SIGTRAP) // doesn't actually work
        }
    }

    fun trace(message: () -> Any?) = clientLogger.trace(message().toString())
    fun debug(message: () -> Any?) = clientLogger.debug(message().toString())
    fun info(message: () -> Any?) = clientLogger.info(message().toString())
    fun warn(message: () -> Any?) = clientLogger.warn(message().toString())
    fun error(message: () -> Any?) = clientLogger.error(message().toString())
    fun critical(message: () -> Any?) = clientLogger.critical(message().toString())
    fun assert(test: Boolean, message: () -> Any? = { null }) {
        if (Platform.isDebugBinary && !test) {
            critical { "Assertion failed${message()?.let { ": $it" } ?: ""}" }
            //raise(SIGTRAP) // doesn't actually work
        }
    }
}
