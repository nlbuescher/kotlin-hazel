@file:Suppress("NOTHING_TO_INLINE")

package opengl

import copengl.GL_FALSE
import copengl.GL_INFO_LOG_LENGTH
import copengl.GL_TRUE
import hazel.math.FloatMatrix4x4
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


// U

internal inline fun glUniformMatrix4fv(location: Int, transpose: Boolean, matrix: FloatMatrix4x4) {
    matrix.toFloatArray().usePinned {
        copengl.glUniformMatrix4fv!!(location, 1, if (transpose) GL_TRUE.convert() else GL_FALSE.convert(), it.addressOf(0))
    }
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
