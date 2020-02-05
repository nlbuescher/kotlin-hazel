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

	fun session(name: String, filepath: String = "profile.json", block: () -> Unit) {
		beginSession(name, filepath)
		block()
		endSession()
	}

	private fun beginSession(name: String, filepath: String) {
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
