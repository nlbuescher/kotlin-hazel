package hazel.renderer.opengl

import copengl.GL_COMPILE_STATUS
import copengl.GL_FALSE
import copengl.GL_FRAGMENT_SHADER
import copengl.GL_LINK_STATUS
import copengl.GL_VERTEX_SHADER
import hazel.core.Hazel
import hazel.math.FloatMatrix3x3
import hazel.math.FloatMatrix4x4
import hazel.math.FloatVector2
import hazel.math.FloatVector3
import hazel.math.FloatVector4
import hazel.renderer.Shader
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.toKString
import kotlinx.cinterop.usePinned
import opengl.glAttachShader
import opengl.glCompileShader
import opengl.glCreateProgram
import opengl.glCreateShader
import opengl.glDeleteProgram
import opengl.glDeleteShader
import opengl.glDetachShader
import opengl.glGetProgramInfoLog
import opengl.glGetProgramiv
import opengl.glGetShaderInfoLog
import opengl.glGetShaderiv
import opengl.glGetUniformLocation
import opengl.glLinkProgram
import opengl.glShaderSource
import opengl.glUniform
import opengl.glUniformMatrix3
import opengl.glUniformMatrix4
import opengl.glUseProgram
import platform.posix.SEEK_END
import platform.posix.SEEK_SET
import platform.posix.fopen
import platform.posix.fread
import platform.posix.fseek
import platform.posix.ftell

class OpenGLShader : Shader {

    override val name: String

    private var rendererId: UInt = 0u

    constructor(filepath: String) {
        val source = readFile(filepath) ?: ""
        val shaderSourcesMap = preProcess(source)
        compile(shaderSourcesMap)

        val nameStart = filepath.lastIndexOfAny(charArrayOf('/', '\\')).let { if (it == -1) 0 else it + 1 }
        val nameEnd = filepath.lastIndexOf('.').let { if (it == -1) filepath.length else it }
        this.name = filepath.substring(nameStart, nameEnd)
    }

    constructor(name: String, vertexSource: String, fragmentSource: String) {
        this.name = name
        val sources = mapOf(GL_VERTEX_SHADER to vertexSource, GL_FRAGMENT_SHADER to fragmentSource)
        compile(sources)
    }

    override fun bind() {
        glUseProgram(rendererId)
    }

    override fun unbind() {
        glUseProgram(0u)
    }

    override fun set(name: String, int: Int) = uploadUniform(name, int)
    override fun set(name: String, vector: FloatVector3) = uploadUniform(name, vector)
    override fun set(name: String, vector: FloatVector4) = uploadUniform(name, vector)
    override fun set(name: String, matrix: FloatMatrix4x4) = uploadUniform(name, matrix)

    fun uploadUniform(name: String, int: Int) {
        val location = glGetUniformLocation(rendererId, name)
        glUniform(location, int)
    }

    fun uploadUniform(name: String, float: Float) {
        val location = glGetUniformLocation(rendererId, name)
        glUniform(location, float)
    }

    fun uploadUniform(name: String, vector: FloatVector2) {
        val location = glGetUniformLocation(rendererId, name)
        glUniform(location, vector.x, vector.y)
    }

    fun uploadUniform(name: String, vector: FloatVector3) {
        val location = glGetUniformLocation(rendererId, name)
        glUniform(location, vector.x, vector.y, vector.z)
    }

    fun uploadUniform(name: String, vector: FloatVector4) {
        val location = glGetUniformLocation(rendererId, name)
        glUniform(location, vector.x, vector.y, vector.z, vector.w)
    }

    fun uploadUniform(name: String, matrix: FloatMatrix3x3) {
        val location = glGetUniformLocation(rendererId, name)
        glUniformMatrix3(location, false, matrix.toFloatArray())
    }

    fun uploadUniform(name: String, matrix: FloatMatrix4x4) {
        val location = glGetUniformLocation(rendererId, name)
        glUniformMatrix4(location, false, matrix.toFloatArray())
    }

    override fun dispose() {
        glDeleteProgram(rendererId)
    }

    private fun readFile(filepath: String): String? {
        return fopen(filepath, "r")?.let { file ->
            fseek(file, 0L, SEEK_END)
            val size = ftell(file).toInt()
            fseek(file, 0L, SEEK_SET)
            ByteArray(size).apply {
                usePinned { fread(it.addressOf(0), 1, size.convert(), file) }
            }.toKString()
        } ?: run {
            Hazel.coreError { "Could not open file `$filepath`" }
            null
        }
    }

    private fun preProcess(source: String): Map<Int, String> {
        val shaderSources = mutableMapOf<Int, String>()
        val typeToken = "#type"
        var pos = source.indexOf(typeToken)
        while (pos != -1) {
            val eol = source.indexOfAny(charArrayOf('\r', '\n'), pos)
            Hazel.coreAssert(eol != -1) { "Syntax error" }
            val begin = pos + typeToken.length + 1
            val type = source.substring(begin, eol)
            Hazel.coreAssert(type.toShaderType() != 0) { "Invalid shader type specified" }

            val nextLinePos = source.indexOfNone(charArrayOf('\r', '\n'), eol)
            pos = source.indexOf(typeToken, nextLinePos)
            shaderSources[type.toShaderType()] = source.substring(nextLinePos, if (pos == -1) source.lastIndex else pos)
        }

        return shaderSources
    }

    private fun compile(shaderSources: Map<Int, String>) {
        val program = glCreateProgram()

        val glShaderIds = MutableList(shaderSources.size) { 0u }

        for ((type, source) in shaderSources) {
            val shader = glCreateShader(type)

            glShaderSource(shader, source)

            glCompileShader(shader)

            if (glGetShaderiv(shader, GL_COMPILE_STATUS) == GL_FALSE) {
                val infoLog = glGetShaderInfoLog(shader)

                glDeleteShader(shader)

                Hazel.coreAssert(false) { "Shader failed to compile!" }
                Hazel.coreError { infoLog }
                break
            }

            glAttachShader(program, shader)
            glShaderIds.add(shader)
        }

        glLinkProgram(program)

        if (glGetProgramiv(program, GL_LINK_STATUS) == GL_FALSE) {
            rendererId = 0u
            val infoLog = glGetProgramInfoLog(rendererId)

            glDeleteProgram(program)

            for (id in glShaderIds)
                glDeleteShader(id)

            Hazel.coreAssert(false) { "Shaders failed to link!" }
            Hazel.coreError { infoLog }
            return

        }

        for (id in glShaderIds)
            glDetachShader(program, id)

        rendererId = program
    }
}

private fun String.toShaderType() = when (this) {
    "vertex" -> GL_VERTEX_SHADER
    "fragment",
    "pixel" -> GL_FRAGMENT_SHADER
    else -> {
        Hazel.coreAssert(false) { "Unknown shader type $this!" }
        0
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
