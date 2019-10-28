@file:Suppress("NOTHING_TO_INLINE")

package opengl

import copengl.GL_FALSE
import copengl.GL_INFO_LOG_LENGTH
import copengl.GL_TRUE
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointerVar
import kotlinx.cinterop.FloatVar
import kotlinx.cinterop.IntVar
import kotlinx.cinterop.UIntVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.cstr
import kotlinx.cinterop.invoke
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.placeTo
import kotlinx.cinterop.ptr
import kotlinx.cinterop.sizeOf
import kotlinx.cinterop.toCPointer
import kotlinx.cinterop.toKString
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value

// convenience functions for kotlin use of OpenGL functions

// A

internal inline fun glAttachShader(program: UInt, shader: UInt) = copengl.glAttachShader!!(program, shader)


// B

internal inline fun glBindBuffer(target: Int, buffer: UInt) = copengl.glBindBuffer!!(target.convert(), buffer)

internal inline fun glBindTextureUnit(unit: Int, texture: UInt) = copengl.glBindTextureUnit!!(unit.convert(), texture)

internal inline fun glBindVertexArray(array: UInt) = copengl.glBindVertexArray!!(array)

internal inline fun glBufferData(target: Int, data: FloatArray, usage: Int) = data.usePinned {
    copengl.glBufferData!!(target.convert(), data.size * sizeOf<FloatVar>(), it.addressOf(0), usage.convert())
}

internal inline fun glBufferData(target: Int, data: UIntArray, usage: Int) = data.usePinned {
    copengl.glBufferData!!(target.convert(), data.size * sizeOf<UIntVar>(), it.addressOf(0), usage.convert())
}


// C

internal inline fun glClear(mask: Int) = copengl.glClear(mask.convert())

internal inline fun glCompileShader(shader: UInt) = copengl.glCompileShader!!(shader)

internal inline fun glCreateBuffer() = glCreateBuffers(1).first()

internal inline fun glCreateBuffers(n: Int) = UIntArray(n).apply {
    usePinned { copengl.glCreateBuffers!!(n, it.addressOf(0)) }
}

internal inline fun glCreateProgram() = copengl.glCreateProgram!!()

internal inline fun glCreateShader(shaderType: Int): UInt = copengl.glCreateShader!!(shaderType.convert())

internal inline fun glCreateTexture(textureType: Int): UInt = glCreateTextures(textureType, 1).first()

internal inline fun glCreateTextures(textureType: Int, n: Int) = UIntArray(n).apply {
    usePinned { copengl.glCreateTextures!!(textureType.convert(), n, it.addressOf(0)) }
}

internal inline fun glCreateVertexArray() = glCreateVertexArrays(1).first()

internal inline fun glCreateVertexArrays(n: Int) = UIntArray(n).apply {
    usePinned { copengl.glCreateVertexArrays!!(n, it.addressOf(0)) }
}


// D

internal inline fun glDeleteBuffer(buffer: UInt) = glDeleteBuffers(1, uintArrayOf(buffer))

internal inline fun glDeleteBuffers(n: Int, buffers: UIntArray) {
    buffers.usePinned { copengl.glDeleteBuffers!!(n, it.addressOf(0)) }
}

internal inline fun glDeleteProgram(program: UInt) = copengl.glDeleteProgram!!(program)

internal inline fun glDeleteShader(shader: UInt) = copengl.glDeleteShader!!(shader)

internal inline fun glDeleteTextures(vararg textures: UInt) = textures.usePinned {
    copengl.glDeleteTextures(textures.size, it.addressOf(0))
}

internal inline fun glDeleteVertexArrays(vararg arrays: UInt) = arrays.usePinned {
    copengl.glDeleteVertexArrays!!(arrays.size, it.addressOf(0))
}

internal inline fun glDetachShader(program: UInt, shader: UInt) = copengl.glDetachShader!!(program, shader)


// E

internal inline fun glEnableVertexAttribArray(index: UInt) = copengl.glEnableVertexAttribArray!!(index)


// G

internal inline fun glGetProgramInfoLog(program: UInt): String {
    val maxLength = glGetProgramiv(program, GL_INFO_LOG_LENGTH)
    return ByteArray(maxLength).apply {
        usePinned { copengl.glGetProgramInfoLog!!(program, maxLength, null, it.addressOf(0)) }
    }.toKString()
}

internal inline fun glGetProgramiv(program: UInt, pname: Int) = memScoped {
    val iv = alloc<IntVar>()
    copengl.glGetProgramiv!!(program, pname.convert(), iv.ptr)
    iv.value
}

internal inline fun glGetShaderInfoLog(shader: UInt): String {
    val maxLength = glGetShaderiv(shader, GL_INFO_LOG_LENGTH)
    return ByteArray(maxLength).apply {
        usePinned { copengl.glGetShaderInfoLog!!(shader, maxLength, null, it.addressOf(0)) }
    }.toKString()
}

internal inline fun glGetShaderiv(shader: UInt, pname: Int): Int = memScoped {
    val iv = alloc<IntVar>()
    copengl.glGetShaderiv!!(shader, pname.convert(), iv.ptr)
    iv.value
}

internal inline fun glGetUniformLocation(program: UInt, name: String): Int = memScoped {
    copengl.glGetUniformLocation!!(program, name.cstr.placeTo(memScope))
}


// L

internal inline fun glLinkProgram(program: UInt) = copengl.glLinkProgram!!(program)


// S

internal inline fun glShaderSource(shader: UInt, string: String) = memScoped {
    val source = alloc<CPointerVar<ByteVar>>()
    source.value = string.cstr.ptr
    copengl.glShaderSource!!(shader, 1, source.ptr, 0L.toCPointer())
}


// T

internal inline fun glTextureParameter(texture: UInt, pname: Int, param: Int) = copengl.glTextureParameteri!!(texture, pname.convert(), param)

internal inline fun glTextureStorage2D(texture: UInt, levels: Int, internalFormat: Int, width: Int, height: Int) {
    copengl.glTextureStorage2D!!(texture, levels, internalFormat.convert(), width, height)
}

internal inline fun glTextureSubImage2D(texture: UInt, level: Int, xOffset: Int, yOffset: Int, width: Int, height: Int, format: Int, type: Int, pixels: ByteArray) = pixels.usePinned {
    copengl.glTextureSubImage2D!!(texture, level, xOffset, yOffset, width, height, format.convert(), type.convert(), it.addressOf(0))
}


// U

internal inline fun glUniform(location: Int, i: Int) = copengl.glUniform1i!!(location, i)

internal inline fun glUniform(location: Int, f: Float) = copengl.glUniform1f!!(location, f)

internal inline fun glUniform(location: Int, f1: Float, f2: Float) = copengl.glUniform2f!!(location, f1, f2)

internal inline fun glUniform(location: Int, f1: Float, f2: Float, f3: Float) = copengl.glUniform3f!!(location, f1, f2, f3)

internal inline fun glUniform(location: Int, f1: Float, f2: Float, f3: Float, f4: Float) = copengl.glUniform4f!!(location, f1, f2, f3, f4)

internal inline fun glUniformMatrix3(location: Int, transpose: Boolean, matrix: FloatArray) = matrix.usePinned {
    copengl.glUniformMatrix3fv!!(location, 1, if (transpose) GL_TRUE.convert() else GL_FALSE.convert(), it.addressOf(0))
}

internal inline fun glUniformMatrix4(location: Int, transpose: Boolean, matrix: FloatArray) = matrix.usePinned {
    copengl.glUniformMatrix4fv!!(location, 1, if (transpose) GL_TRUE.convert() else GL_FALSE.convert(), it.addressOf(0))
}

internal inline fun glUseProgram(program: UInt) = copengl.glUseProgram!!(program)


// V

internal inline fun glVertexAttribPointer(index: UInt, size: Int, type: Int, normalized: Boolean, stride: Int, pointer: Int) {
    copengl.glVertexAttribPointer!!(
        index,
        size,
        type.convert(),
        if (normalized) GL_TRUE.convert() else GL_FALSE.convert(),
        stride,
        pointer.toLong().toCPointer()
    )
}
