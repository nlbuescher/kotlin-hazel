package hazel.debug

import hazel.core.Hazel
import kotlinx.cinterop.CPointer
import platform.posix.FILE
import platform.posix.fclose
import platform.posix.fflush
import platform.posix.fopen
import platform.posix.fprintf

data class ProfileResult(
    val name: String,
    val start: Long,
    val end: Long,
    val threadId: UInt
)

data class InstrumentationSession(val name: String)

private var currentSession: InstrumentationSession? = null
private var profileCount: Int = 0
private var file: CPointer<FILE>? = null

object Instrumentor {

    fun session(name: String, filepath: String = "profile_result.json", block: () -> Unit) {
        beginSession(name, filepath)
        block()
        endSession()
    }

    private fun beginSession(name: String, filepath: String = "profile_result.json") {
        if (Hazel.config.isProfileEnabled) {
            file = fopen(filepath, "w")
            writeHeader()
            currentSession = InstrumentationSession(name)
        }
    }

    private fun endSession() {
        if (Hazel.config.isProfileEnabled) {
            writeFooter()
            fclose(file)
            currentSession = null
            profileCount = 0
        }
    }

    internal fun writeProfile(result: ProfileResult) {
        if (profileCount > 0)
            fprintf(file, ",")

        profileCount += 1

        val name = result.name.replace("'", "\\'")

        fprintf(file, "{")
        fprintf(file, "\"cat\":\"function\",")
        fprintf(file, "\"dur\":${result.end - result.start},")
        fprintf(file, "\"name\":\"$name\",")
        fprintf(file, "\"ph\":\"X\",")
        fprintf(file, "\"pid\":0,")
        fprintf(file, "\"tid\":${result.threadId},")
        fprintf(file, "\"ts\":${result.start}")
        fprintf(file, "}")

        fflush(file)
    }

    private fun writeHeader() {
        fprintf(file, "{\"otherData\":{},\"traceEvents\":[")
        fflush(file)
    }

    private fun writeFooter() {
        fprintf(file, "]}")
        fflush(file)
    }
}
