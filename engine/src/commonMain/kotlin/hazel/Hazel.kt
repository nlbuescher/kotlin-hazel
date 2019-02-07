package hazel

import hazel.logging.Logger


private val coreLogger = Logger("HAZEL")
private val clientLogger = Logger("APP")


@ExperimentalUnsignedTypes
object Hazel {
    fun run(app: Application) {
        coreWarn("Initialized Log!")
        info("Hello!")

        app.run()
    }

    internal fun coreTrace(message: String) = coreLogger.trace(message)
    internal fun coreDebug(message: String) = coreLogger.debug(message)
    internal fun coreInfo(message: String) = coreLogger.info(message)
    internal fun coreWarn(message: String) = coreLogger.warn(message)
    internal fun coreError(message: String) = coreLogger.error(message)
    internal fun coreCritical(message: String) = coreLogger.critical(message)

    internal fun coreAssert(test: Boolean, message: String) = check(test) { coreError("Assertion failed: $message") }

    fun trace(message: String) = clientLogger.trace(message)
    fun debug(message: String) = clientLogger.debug(message)
    fun info(message: String) = clientLogger.info(message)
    fun warn(message: String) = clientLogger.warn(message)
    fun error(message: String) = clientLogger.error(message)
    fun critical(message: String) = clientLogger.critical(message)

    fun assert(test: Boolean, message: String) = check(test) { error("Assertion failed: $message") }
}
