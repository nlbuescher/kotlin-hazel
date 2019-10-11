package opengl

import copengl.GL_FALSE
import copengl.GL_INFO_LOG_LENGTH
import copengl.GL_TRUE
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CPointerVar
import kotlinx.cinterop.IntVar
import kotlinx.cinterop.UIntVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.convert
import kotlinx.cinterop.cstr
import kotlinx.cinterop.invoke
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toCPointer
import kotlinx.cinterop.toKString
import kotlinx.cinterop.value

// A

fun glAttachShader(program: UInt, shader: UInt) = copengl.glAttachShader!!(program, shader)


// B

fun glBindBuffer(target: Int, buffer: UInt) = copengl.glBindBuffer!!(target.convert(), buffer)

fun glBindVertexArray(array: UInt) = copengl.glBindVertexArray!!(array)

fun glBufferData(
    target: Int,
    size: Long,
    data: COpaquePointer,
    usage: Int
) = copengl.glBufferData!!(target.convert(), size, data, usage.convert())


// C

fun glCompileShader(shader: UInt) = copengl.glCompileShader!!(shader)

fun glCreateProgram() = copengl.glCreateProgram!!()

fun glCreateShader(shaderType: Int): UInt = copengl.glCreateShader!!(shaderType.convert())


// D

fun glDeleteProgram(program: UInt) = copengl.glDeleteProgram!!(program)

fun glDeleteShader(shader: UInt) = copengl.glDeleteShader!!(shader)

fun glDetachShader(program: UInt, shader: UInt) = copengl.glDetachShader!!(program, shader)


// E

fun glEnableVertexAttribArray(index: UInt) = copengl.glEnableVertexAttribArray!!(index)


// G

fun glGenBuffer() = memScoped {
    val b = alloc<UIntVar>()
    glGenBuffers(1, b.ptr)
    b.value
}

fun glGenBuffers(n: Int, buffers: CPointer<UIntVar>) = copengl.glGenBuffers!!(n, buffers)

fun glGenVertexArray() = memScoped {
    val va = alloc<UIntVar>()
    glGenVertexArrays(1, va.ptr)
    va.value
}

fun glGenVertexArrays(n: Int, arrays: CPointer<UIntVar>) = copengl.glGenVertexArrays!!(n, arrays)

fun glGetProgramInfoLog(program: UInt) = memScoped {
    val maxLength = glGetProgramiv(program, GL_INFO_LOG_LENGTH)
    val length = alloc<IntVar>()
    val infoLog = allocArray<ByteVar>(maxLength)
    copengl.glGetProgramInfoLog!!(program, maxLength, length.ptr, infoLog)
    infoLog.toKString()
}

fun glGetProgramiv(program: UInt, pname: Int) = memScoped {
    val iv = alloc<IntVar>()
    copengl.glGetProgramiv!!(program, pname.convert(), iv.ptr)
    iv.value
}

fun glGetShaderInfoLog(shader: UInt) = memScoped {
    val maxLength = glGetShaderiv(shader, GL_INFO_LOG_LENGTH)
    val length = alloc<IntVar>()
    val infoLog = allocArray<ByteVar>(maxLength)
    copengl.glGetShaderInfoLog!!(shader, maxLength, length.ptr, infoLog)
    infoLog.toKString()
}

fun glGetShaderiv(shader: UInt, pname: Int): Int = memScoped {
    val iv = alloc<IntVar>()
    copengl.glGetShaderiv!!(shader, pname.convert(), iv.ptr)
    iv.value
}


// L

fun glLinkProgram(program: UInt) = copengl.glLinkProgram!!(program)


// S

fun glShaderSource(shader: UInt, string: String) = memScoped {
    val source = alloc<CPointerVar<ByteVar>>()
    source.value = string.cstr.ptr
    copengl.glShaderSource!!(shader, 1, source.ptr, 0L.toCPointer())
}


// U

fun glUseProgram(program: UInt) = copengl.glUseProgram!!(program)


// V

fun glVertexAttribPointer(
    index: UInt,
    size: Int,
    type: Int,
    normalized: Boolean,
    stride: Int,
    pointer: Long
) = copengl.glVertexAttribPointer!!(
    index,
    size,
    type.convert(),
    if (normalized) GL_TRUE.convert() else GL_FALSE.convert(),
    stride,
    pointer.toCPointer()
)
