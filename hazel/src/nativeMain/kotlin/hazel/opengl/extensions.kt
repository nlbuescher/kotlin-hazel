@file:Suppress("NOTHING_TO_INLINE")

package hazel.opengl

import com.kgl.opengl.*
import hazel.math.*
import kotlinx.cinterop.*

// convenience functions for kotlin use of OpenGL functions
// these functions convert kotlin types to the proper cinterop types


// B

internal inline fun glBufferData(target: UInt, byteCount: Int, usage: UInt) {
	glBufferData(target, byteCount.toLong(), null, usage)
}

internal inline fun glBufferData(target: UInt, data: FloatArray, usage: UInt) {
	glBufferData(target, data.size * sizeOf<FloatVar>(), data.refTo(0), usage)
}

internal inline fun glBufferData(target: UInt, data: UIntArray, usage: UInt) {
	glBufferData(target, data.size * sizeOf<UIntVar>(), data.refTo(0), usage)
}

internal inline fun glBufferSubData(target: UInt, offset: Int, size: Int, data: ByteArray) {
	glBufferSubData(target, offset.convert(), size.convert(), data.refTo(0))
}


// D

internal inline fun glDeleteBuffers(vararg buffers: UInt) {
	glDeleteBuffers(buffers.size, buffers.refTo(0))
}

internal inline fun glDeleteTextures(vararg textures: UInt) {
	glDeleteTextures(textures.size, textures.refTo(0))
}

internal inline fun glDeleteVertexArrays(vararg arrays: UInt) {
	glDeleteVertexArrays(arrays.size, arrays.refTo(0))
}


// G

internal inline fun glGetProgramUInt(program: UInt, pname: UInt): UInt = memScoped {
	val iv = alloc<IntVar>()
	glGetProgramiv(program, pname, iv.ptr)
	iv.value.convert()
}

internal inline fun glGetShaderUInt(shader: UInt, pname: UInt): UInt = memScoped {
	val iv = alloc<IntVar>()
	glGetShaderiv(shader, pname, iv.ptr)
	iv.value.convert()
}


// T

internal inline fun glTexImage2D(
	texture: UInt,
	level: Int,
	internalFormat: UInt,
	width: Int,
	height: Int,
	border: Int,
	format: UInt,
	pixels: ByteArray?
) {
	glTexImage2D(
		texture,
		level,
		internalFormat.convert(),
		width,
		height,
		border,
		format,
		GL_UNSIGNED_BYTE,
		pixels?.refTo(0)
	)
}

internal inline fun glTexParameter(texture: UInt, pname: UInt, param: UInt) {
	glTexParameteri(texture, pname, param.convert())
}


// U

internal inline fun glUniform(location: Int, i: Int) {
	glUniform1i(location, i)
}

internal inline fun glUniform(location: Int, f: Float) {
	glUniform1f(location, f)
}

internal inline fun glUniform(location: Int, f1: Float, f2: Float) {
	glUniform2f(location, f1, f2)
}

internal inline fun glUniform(location: Int, f1: Float, f2: Float, f3: Float) {
	glUniform3f(location, f1, f2, f3)
}

internal inline fun glUniform(location: Int, f1: Float, f2: Float, f3: Float, f4: Float) {
	glUniform4f(location, f1, f2, f3, f4)
}

internal inline fun glUniform(location: Int, transpose: Boolean, matrix: Mat4) {
	glUniformMatrix4fv(location, 1, transpose, (matrix as MutableMat4).asFloatArray().refTo(0))
}


// V

internal inline fun glVertexAttribPointer(
	index: UInt,
	size: Int,
	type: UInt,
	normalized: Boolean,
	stride: Int,
	pointer: Int
) {
	glVertexAttribPointer(
		index,
		size,
		type.convert(),
		normalized,
		stride,
		pointer.toLong().toCPointer<CPointed>()
	)
}
