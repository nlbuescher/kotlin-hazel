package hazel.renderer.opengl

import com.kgl.opengl.*
import hazel.core.Hazel
import hazel.core.coreAssert
import hazel.core.coreError
import hazel.core.profile
import hazel.math.Mat4
import hazel.math.Vec2
import hazel.math.Vec3
import hazel.math.Vec4
import hazel.opengl.glGetProgramUInt
import hazel.opengl.glGetShaderUInt
import hazel.opengl.glUniform
import hazel.renderer.Shader
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.toKString
import kotlinx.cinterop.usePinned
import platform.posix.*

class OpenGLShader : Shader {
	private var rendererId: UInt = 0u

	override val name: String

	constructor(filepath: String) {
		val profiler = Hazel.Profiler("OpenGLShader(String): OpenGLShader")
		profiler.start()

		val source = readFile(filepath) ?: ""
		val shaderSourcesMap = preProcess(source)
		compile(shaderSourcesMap)

		val nameStart = filepath.lastIndexOfAny(charArrayOf('/', '\\')).let { if (it == -1) 0 else it + 1 }
		val nameEnd = filepath.lastIndexOf('.').let { if (it == -1) filepath.length else it }
		this.name = filepath.substring(nameStart, nameEnd)

		profiler.stop()
	}

	constructor(name: String, vertexSource: String, fragmentSource: String) {
		val profiler = Hazel.Profiler("OpenGLShader(String, String, String): OpenGLShader")
		profiler.start()

		this.name = name
		val sources = mapOf(
			GL_VERTEX_SHADER to vertexSource,
			GL_FRAGMENT_SHADER to fragmentSource
		)
		compile(sources)

		profiler.stop()
	}

	override fun dispose() {
		Hazel.profile("OpenGLShader.dispose()") {
			glDeleteProgram(rendererId)
		}
	}

	override fun bind() {
		Hazel.profile("OpenGLShader.bind()") {
			glUseProgram(rendererId)
		}
	}

	override fun unbind() {
		Hazel.profile("OpenGLShader.unbind()") {
			glUseProgram(0u)
		}
	}

	override fun set(name: String, int: Int) {
		Hazel.profile("OpenGLShader.set(String, Int)") {
			uploadUniform(name, int)
		}
	}

	override fun set(name: String, float: Float) {
		Hazel.profile("OpenGLShader.set(String, Float)") {
			uploadUniform(name, float)
		}
	}

	override fun set(name: String, vector: Vec3) {
		Hazel.profile("OpenGLShader.set(String, Vec3)") {
			uploadUniform(name, vector)
		}
	}

	override fun set(name: String, vector: Vec4) {
		Hazel.profile("OpenGLShader.set(String, Vec4)") {
			uploadUniform(name, vector)
		}
	}

	override fun set(name: String, matrix: Mat4) {
		Hazel.profile("OpenGLShader.set(String, Mat4)") {
			uploadUniform(name, matrix)
		}
	}

	fun uploadUniform(name: String, int: Int) {
		val location = glGetUniformLocation(rendererId, name)
		glUniform(location, int)
	}

	fun uploadUniform(name: String, float: Float) {
		val location = glGetUniformLocation(rendererId, name)
		glUniform(location, float)
	}

	fun uploadUniform(name: String, vector: Vec2) {
		val location = glGetUniformLocation(rendererId, name)
		glUniform(location, vector.x, vector.y)
	}

	fun uploadUniform(name: String, vector: Vec3) {
		val location = glGetUniformLocation(rendererId, name)
		glUniform(location, vector.x, vector.y, vector.z)
	}

	fun uploadUniform(name: String, vector: Vec4) {
		val location = glGetUniformLocation(rendererId, name)
		glUniform(location, vector.x, vector.y, vector.z, vector.w)
	}

	fun uploadUniform(name: String, matrix: Mat4) {
		val location = glGetUniformLocation(rendererId, name)
		glUniform(location, false, matrix)
	}

	private fun readFile(filepath: String): String? {
		return Hazel.profile("OpenGLShader.readFile(String): String?") {
			fopen(filepath, "r")?.let { file ->
				fseek(file, 0, SEEK_END)
				val size = ftell(file).toInt()
				fseek(file, 0, SEEK_SET)
				ByteArray(size).apply {
					usePinned { fread(it.addressOf(0), 1.convert(), size.convert(), file) }
				}.toKString()
			} ?: run {
				Hazel.coreError("Could not open file `$filepath`")
				null
			}
		}
	}

	private fun preProcess(source: String): Map<UInt, String> {
		return Hazel.profile("OpenGLShader.preProcess(String): Map<UInt, String>") {
			val shaderSources = mutableMapOf<UInt, String>()
			val typeToken = "#type"
			var pos = source.indexOf(typeToken)
			while (pos != -1) {
				val eol = source.indexOfAny(charArrayOf('\r', '\n'), pos)
				Hazel.coreAssert(eol != -1, "Syntax error")
				val begin = pos + typeToken.length + 1
				val type = source.substring(begin, eol)
				Hazel.coreAssert(type.toShaderType() != 0u, "Invalid shader type specified")

				val nextLinePos = source.indexOfNone(charArrayOf('\r', '\n'), eol)
				pos = source.indexOf(typeToken, nextLinePos)
				shaderSources[type.toShaderType()] = source.substring(nextLinePos, if (pos == -1) source.lastIndex else pos)
			}

			shaderSources
		}
	}

	private fun compile(shaderSources: Map<UInt, String>) {
		Hazel.profile("OpenGLShader.compile(Map<UInt, String>)") {
			val program = glCreateProgram()

			val glShaderIds = MutableList(shaderSources.size) { 0u }

			for ((type, source) in shaderSources) {
				val shader = glCreateShader(type)

				glShaderSource(shader, source)

				glCompileShader(shader)

				if (glGetShaderUInt(shader, GL_COMPILE_STATUS) == GL_FALSE) {
					val infoLog = glGetShaderInfoLog(shader)

					glDeleteShader(shader)

					Hazel.coreError(infoLog)
					Hazel.coreAssert(false, "Shader failed to compile!")
					break
				}

				glAttachShader(program, shader)
				glShaderIds.add(shader)
			}

			glLinkProgram(program)

			if (glGetProgramUInt(program, GL_LINK_STATUS) == GL_FALSE) {
				rendererId = 0u
				val infoLog = glGetProgramInfoLog(rendererId)

				glDeleteProgram(program)

				for (id in glShaderIds)
					glDeleteShader(id)

				Hazel.coreError(infoLog)
				Hazel.coreAssert(false, "Shaders failed to link!")
				return@profile
			}

			for (id in glShaderIds)
				glDetachShader(program, id)

			rendererId = program
		}
	}


	private fun String.toShaderType(): UInt = when (this) {
		"vertex" -> GL_VERTEX_SHADER
		"fragment",
		"pixel" -> GL_FRAGMENT_SHADER
		else -> {
			Hazel.coreAssert(false, "Unknown shader type $this!")
			0u
		}
	}

	/**
	 * Finds the index of the first occurrence of any character this is not one of the specified [chars] in this char
	 * sequence, starting from the specified [startIndex] and optionally ignoring the case.
	 *
	 * @param ignoreCase `true` to ignore character case when matching a character. By default `false`.
	 * @return An index of the first occurrence of matched character not from [chars] or -1 if no match is found.
	 */
	private fun CharSequence.indexOfNone(chars: CharArray, startIndex: Int = 0, ignoreCase: Boolean = false): Int {
		for (index in startIndex.coerceAtLeast(0)..lastIndex) {
			val charAtIndex = get(index)
			if (chars.any { !it.equals(charAtIndex, ignoreCase) })
				return index
		}
		return -1
	}
}
