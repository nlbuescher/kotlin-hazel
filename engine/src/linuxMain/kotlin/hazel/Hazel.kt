package hazel

import hazel.logging.clientLogger
import hazel.logging.coreLogger

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

    fun trace(message: String) = clientLogger.trace(message)
    fun debug(message: String) = clientLogger.debug(message)
    fun info(message: String) = clientLogger.info(message)
    fun warn(message: String) = clientLogger.warn(message)
    fun error(message: String) = clientLogger.error(message)
    fun critical(message: String) = clientLogger.critical(message)
}