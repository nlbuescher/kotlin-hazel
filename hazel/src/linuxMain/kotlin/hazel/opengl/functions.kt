@file:Suppress("NOTHING_TO_INLINE")

package hazel.opengl

import com.kgl.opengl.GL_FALSE
import com.kgl.opengl.GL_TRUE
import kotlinx.cinterop.CPointed
import kotlinx.cinterop.FloatVar
import kotlinx.cinterop.IntVar
import kotlinx.cinterop.UIntVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.cstr
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pin
import kotlinx.cinterop.placeTo
import kotlinx.cinterop.ptr
import kotlinx.cinterop.sizeOf
import kotlinx.cinterop.toCPointer
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value

// convenience functions for kotlin use of OpenGL functions
// these functions convert kotlin types to the proper cinterop types


// B

internal inline fun glBufferData(target: UInt, data: FloatArray, usage: UInt) = data.usePinned {
    com.kgl.opengl.glBufferData(target, data.size * sizeOf<FloatVar>(), it.addressOf(0), usage)
}

internal inline fun glBufferData(target: UInt, data: UIntArray, usage: UInt) = data.usePinned {
    com.kgl.opengl.glBufferData(target, data.size * sizeOf<UIntVar>(), it.addressOf(0), usage)
}


// C

internal inline fun glCreateBuffer() = glCreateBuffers(1).first()

internal inline fun glCreateBuffers(n: Int) = UIntArray(n).apply {
    usePinned { com.kgl.opengl.glCreateBuffers(n, it.addressOf(0)) }
}

internal inline fun glCreateTexture(textureType: UInt): UInt = glCreateTextures(textureType, 1).first()

internal inline fun glCreateTextures(textureType: UInt, n: Int) = UIntArray(n).apply {
    usePinned { com.kgl.opengl.glCreateTextures(textureType, n, it.addressOf(0)) }
}

internal inline fun glCreateVertexArray() = glCreateVertexArrays(1).first()

internal inline fun glCreateVertexArrays(n: Int) = UIntArray(n).apply {
    usePinned { com.kgl.opengl.glCreateVertexArrays(n, it.addressOf(0)) }
}


// D

internal inline fun glDeleteBuffer(buffer: UInt) = glDeleteBuffers(1, uintArrayOf(buffer))

internal inline fun glDeleteBuffers(n: Int, buffers: UIntArray) {
    buffers.usePinned { com.kgl.opengl.glDeleteBuffers(n, it.addressOf(0)) }
}

internal inline fun glDeleteTextures(vararg textures: UInt) = textures.usePinned {
    com.kgl.opengl.glDeleteTextures(textures.size, it.addressOf(0))
}

internal inline fun glDeleteVertexArrays(vararg arrays: UInt) = arrays.usePinned {
    com.kgl.opengl.glDeleteVertexArrays(arrays.size, it.addressOf(0))
}


// G

internal inline fun glGetProgramiv(program: UInt, pname: UInt): UInt = memScoped {
    val iv = alloc<IntVar>()
    com.kgl.opengl.glGetProgramiv(program, pname, iv.ptr)
    iv.value.convert()
}

internal inline fun glGetShaderiv(shader: UInt, pname: UInt): UInt = memScoped {
    val iv = alloc<IntVar>()
    com.kgl.opengl.glGetShaderiv(shader, pname, iv.ptr)
    iv.value.convert()
}

internal inline fun glGetUniformLocation(program: UInt, name: String): Int = memScoped {
    com.kgl.opengl.glGetUniformLocation(program, name.cstr.placeTo(memScope))
}


// T

internal inline fun glTextureParameter(texture: UInt, pname: UInt, param: UInt) = com.kgl.opengl.glTextureParameteri(texture, pname, param.convert())

internal inline fun glTextureSubImage2D(texture: UInt, level: Int, xOffset: Int, yOffset: Int, width: Int, height: Int, format: UInt, type: UInt, pixels: ByteArray?) {
    val data = pixels?.pin()
    try {
        com.kgl.opengl.glTextureSubImage2D(texture, level, xOffset, yOffset, width, height, format, type, data?.addressOf(0))
    } finally {
        data?.unpin()
    }
}


// U

internal inline fun glUniform(location: Int, i: Int) = com.kgl.opengl.glUniform1i(location, i)

internal inline fun glUniform(location: Int, f: Float) = com.kgl.opengl.glUniform1f(location, f)

internal inline fun glUniform(location: Int, f1: Float, f2: Float) = com.kgl.opengl.glUniform2f(location, f1, f2)

internal inline fun glUniform(location: Int, f1: Float, f2: Float, f3: Float) = com.kgl.opengl.glUniform3f(location, f1, f2, f3)

internal inline fun glUniform(location: Int, f1: Float, f2: Float, f3: Float, f4: Float) = com.kgl.opengl.glUniform4f(location, f1, f2, f3, f4)

internal inline fun glUniformMatrix3(location: Int, transpose: Boolean, matrix: FloatArray) = matrix.usePinned {
    com.kgl.opengl.glUniformMatrix3fv(location, 1, (if (transpose) GL_TRUE else GL_FALSE).convert(), it.addressOf(0))
}

internal inline fun glUniformMatrix4(location: Int, transpose: Boolean, matrix: FloatArray) = matrix.usePinned {
    com.kgl.opengl.glUniformMatrix4fv(location, 1, (if (transpose) GL_TRUE else GL_FALSE).convert(), it.addressOf(0))
}


// V

internal inline fun glVertexAttribPointer(index: UInt, size: Int, type: UInt, normalized: Boolean, stride: Int, pointer: Int) {
    com.kgl.opengl.glVertexAttribPointer(index, size, type.convert(), (if (normalized) GL_TRUE else GL_FALSE).convert(), stride, pointer.toLong().toCPointer<CPointed>())
}
