package hazel

import kotlin.native.concurrent.ThreadLocal
import kotlin.time.MonoClock

@ThreadLocal
object Hazel {
    private val coreLogger = Logger("HAZEL")
    private val clientLogger = Logger("APP")
    val application: Application get() = Application.INSTANCE ?: kotlin.error("must instantiate an Application first!")


    fun run() {
        coreWarn("Initialized Log!")
        info("Hello!")

        application.run()
    }


    internal fun coreTrace(message: Any?) = coreLogger.trace(message)
    internal fun coreDebug(message: Any?) = coreLogger.debug(message)
    internal fun coreInfo(message: Any?) = coreLogger.info(message)
    internal fun coreWarn(message: Any?) = coreLogger.warn(message)
    internal fun coreError(message: Any?) = coreLogger.error(message)
    internal fun coreCritical(message: Any?) = coreLogger.critical(message)

    internal fun coreAssert(test: Boolean, message: Any? = null) = check(test) { coreCritical("Assertion failed: $message") }

    fun trace(message: Any?) = clientLogger.trace(message)
    fun debug(message: Any?) = clientLogger.debug(message)
    fun info(message: Any?) = clientLogger.info(message)
    fun warn(message: Any?) = clientLogger.warn(message)
    fun error(message: Any?) = clientLogger.error(message)
    fun critical(message: Any?) = clientLogger.critical(message)

    fun assert(test: Boolean, message: Any? = null) = check(test) { critical("Assertion failed: $message") }

    private val start = MonoClock.markNow()
    val time get() = start.elapsedNow().inSeconds
}
