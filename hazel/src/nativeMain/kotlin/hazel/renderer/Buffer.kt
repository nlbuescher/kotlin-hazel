package hazel.renderer

import hazel.Disposable
import hazel.renderer.opengl.OpenGLIndexBuffer
import hazel.renderer.opengl.OpenGLVertexBuffer

// TODO build DSL for building BufferLayouts

enum class ShaderDataType(val size: kotlin.Int) {
    Boolean(1),
    Int(4), Int2(8), Int3(12), Int4(16),
    Float(4), Float2(8), Float3(12), Float4(16),
    Mat3(36), Mat4(64)
}

class BufferElement(val type: ShaderDataType, val name: String, val isNormalized: Boolean = false) {
    val size: Int get() = type.size
    var offset: Int = 0

    val componentCount
        get() = when (type) {
            ShaderDataType.Boolean -> 1
            ShaderDataType.Int -> 1
            ShaderDataType.Int2 -> 2
            ShaderDataType.Int3 -> 3
            ShaderDataType.Int4 -> 4
            ShaderDataType.Float -> 1
            ShaderDataType.Float2 -> 2
            ShaderDataType.Float3 -> 3
            ShaderDataType.Float4 -> 4
            ShaderDataType.Mat3 -> 9
            ShaderDataType.Mat4 -> 16
        }
}

class BufferLayout(vararg elements: BufferElement) {
    val elements = listOf(*elements)

    val stride: Int

    init {
        var stride = 0
        var offset = 0
        elements.forEach {
            it.offset = offset
            offset += it.size
            stride += it.size
        }
        this.stride = stride
    }
}

interface VertexBuffer : Disposable {
    fun bind()
    fun unbind()

    var layout: BufferLayout
}

fun vertexBufferOf(vararg vertices: Float): VertexBuffer {
    return when (Renderer.renderAPI) {
        RenderAPI.None -> TODO("RenderAPI.None is currently not supported")
        RenderAPI.OpenGL -> OpenGLVertexBuffer(vertices)
    }
}

interface IndexBuffer : Disposable {
    val count: Int
    fun bind()
    fun unbind()
}

fun indexBufferOf(vararg indices: UInt): IndexBuffer {
    return when (Renderer.renderAPI) {
        RenderAPI.None -> TODO("RenderAPI.None is currently not supported")
        RenderAPI.OpenGL -> OpenGLIndexBuffer(indices)
    }
}
