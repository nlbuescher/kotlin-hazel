package hazel.debug

import hazel.core.*
import kotlinx.cinterop.*
import platform.posix.*

data class ProfileResult(
	val name: String,
	val start: Long,
	val end: Long,
	val threadId: ULong
)

data class InstrumentationSession(val name: String)

@ThreadLocal
object Instrumentor {
	private var currentSession: InstrumentationSession? = null
	private var profileCount: Int = 0
	private var file: CPointer<FILE>? = null

	fun session(name: String, filepath: String = "profile.json", block: () -> Unit) {
		beginSession(name, filepath)
		block()
		endSession()
	}

	private fun beginSession(name: String, filepath: String) {
		if (Hazel.Config.enableProfiling) {
			file = fopen(filepath, "w")
			writeHeader()
			currentSession = InstrumentationSession(name)
		}
	}

	private fun endSession() {
		if (Hazel.Config.enableProfiling) {
			writeFooter()
			fclose(file)
			currentSession = null
			profileCount = 0
		}
	}

	fun writeProfile(result: ProfileResult) {
		file?.let {
			if (profileCount > 0)
				fprintf(it, ",")

			profileCount += 1

			val name = result.name.replace('"', '\'')

			fprintf(it, "{")
			fprintf(it, "\"cat\":\"function\",")
			fprintf(it, "\"dur\":${result.end - result.start},")
			fprintf(it, "\"name\":\"$name\",")
			fprintf(it, "\"ph\":\"X\",")
			fprintf(it, "\"pid\":0,")
			fprintf(it, "\"tid\":${result.threadId},")
			fprintf(it, "\"ts\":${result.start}")
			fprintf(it, "}")

			fflush(it)
		}
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
