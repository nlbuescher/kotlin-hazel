@file:Suppress("NOTHING_TO_INLINE")

package hazel.opengl

import com.kgl.opengl.GL_UNSIGNED_BYTE
import hazel.math.Mat4
import hazel.math.MutableMat4
import kotlinx.cinterop.*

// convenience functions for kotlin use of OpenGL functions
// these functions convert kotlin types to the proper cinterop types


// B

internal inline fun glBufferData(target: UInt, byteCount: Int, usage: UInt) {
	com.kgl.opengl.glBufferData(target, byteCount.toLong(), null, usage)
}

internal inline fun glBufferData(target: UInt, data: FloatArray, usage: UInt) {
	com.kgl.opengl.glBufferData(target, data.size * sizeOf<FloatVar>(), data.refTo(0), usage)
}

internal inline fun glBufferData(target: UInt, data: UIntArray, usage: UInt) {
	com.kgl.opengl.glBufferData(target, data.size * sizeOf<UIntVar>(), data.refTo(0), usage)
}

internal inline fun glBufferSubData(target: UInt, offset: Int, size: Int, data: ByteArray) {
	com.kgl.opengl.glBufferSubData(target, offset.convert(), size.convert(), data.refTo(0))
}


// D

internal inline fun glDeleteBuffers(vararg buffers: UInt) {
	com.kgl.opengl.glDeleteBuffers(buffers.size, buffers.refTo(0))
}

internal inline fun glDeleteTextures(vararg textures: UInt) {
	com.kgl.opengl.glDeleteTextures(textures.size, textures.refTo(0))
}

internal inline fun glDeleteVertexArrays(vararg arrays: UInt) {
	com.kgl.opengl.glDeleteVertexArrays(arrays.size, arrays.refTo(0))
}


// G

internal inline fun glGetProgramUInt(program: UInt, pname: UInt): UInt = memScoped {
	val iv = alloc<IntVar>()
	com.kgl.opengl.glGetProgramiv(program, pname, iv.ptr)
	iv.value.convert()
}

internal inline fun glGetShaderUInt(shader: UInt, pname: UInt): UInt = memScoped {
	val iv = alloc<IntVar>()
	com.kgl.opengl.glGetShaderiv(shader, pname, iv.ptr)
	iv.value.convert()
}


// T

internal inline fun glTexImage2D(texture: UInt, level: Int, internalFormat: UInt, width: Int, height: Int, border: Int, format: UInt, pixels: ByteArray?) {
	com.kgl.opengl.glTexImage2D(texture, level, internalFormat.convert(), width, height, border, format, GL_UNSIGNED_BYTE, pixels?.refTo(0))
}

internal inline fun glTexParameter(texture: UInt, pname: UInt, param: UInt) {
	com.kgl.opengl.glTexParameteri(texture, pname, param.convert())
}


// U

internal inline fun glUniform(location: Int, i: Int) {
	com.kgl.opengl.glUniform1i(location, i)
}

internal inline fun glUniform(location: Int, f: Float) {
	com.kgl.opengl.glUniform1f(location, f)
}

internal inline fun glUniform(location: Int, f1: Float, f2: Float) {
	com.kgl.opengl.glUniform2f(location, f1, f2)
}

internal inline fun glUniform(location: Int, f1: Float, f2: Float, f3: Float) {
	com.kgl.opengl.glUniform3f(location, f1, f2, f3)
}

internal inline fun glUniform(location: Int, f1: Float, f2: Float, f3: Float, f4: Float) {
	com.kgl.opengl.glUniform4f(location, f1, f2, f3, f4)
}

internal inline fun glUniform(location: Int, transpose: Boolean, matrix: Mat4) {
	com.kgl.opengl.glUniformMatrix4fv(location, 1, transpose, (matrix as MutableMat4).asFloatArray().refTo(0))
}


// V

internal inline fun glVertexAttribPointer(index: UInt, size: Int, type: UInt, normalized: Boolean, stride: Int, pointer: Int) {
	com.kgl.opengl.glVertexAttribPointer(index, size, type.convert(), normalized, stride, pointer.toLong().toCPointer<CPointed>())
}
