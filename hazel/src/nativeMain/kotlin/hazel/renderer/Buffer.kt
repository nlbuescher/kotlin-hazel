package hazel.renderer

import hazel.core.*
import hazel.renderer.opengl.*

// TODO build DSL for building BufferLayouts

enum class ShaderDataType(val size: kotlin.Int) {
	Boolean(1),
	Int(4), Int2(8), Int3(12), Int4(16),
	Float(4), Float2(8), Float3(12), Float4(16),
	Mat3(3), Mat4(4)
}

class BufferElement(
	val type: ShaderDataType,
	@Suppress("unused")
	val name: String,
	val isNormalized: Boolean = false
) {
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
	var layout: BufferLayout

	fun bind()
	fun unbind()

	fun setData(data: ByteArray, size: Int = data.size)
}

@Suppress("FunctionName")
fun VertexBuffer(byteCount: Int): VertexBuffer = when (Renderer.api) {
	RenderAPI.API.None -> TODO("RenderAPI.API.None is currently not supported")
	RenderAPI.API.OpenGL -> OpenGLVertexBuffer(byteCount)
}

fun vertexBufferOf(vararg vertices: Float): VertexBuffer = when (Renderer.api) {
	RenderAPI.API.None -> TODO("RenderAPI.API.None is currently not supported")
	RenderAPI.API.OpenGL -> OpenGLVertexBuffer(vertices)
}

// currently, hazel only supports 32 bit index buffers
interface IndexBuffer : Disposable {
	val count: Int
	fun bind()
	fun unbind()
}

@Suppress("FunctionName")
fun IndexBuffer(indices: UIntArray): IndexBuffer = when (Renderer.api) {
	RenderAPI.API.None -> TODO("RenderAPI.API.None is currently not supported")
	RenderAPI.API.OpenGL -> OpenGLIndexBuffer(indices)
}

fun indexBufferOf(vararg indices: UInt): IndexBuffer = when (Renderer.api) {
	RenderAPI.API.None -> TODO("RenderAPI.API.None is currently not supported")
	RenderAPI.API.OpenGL -> OpenGLIndexBuffer(indices)
}
