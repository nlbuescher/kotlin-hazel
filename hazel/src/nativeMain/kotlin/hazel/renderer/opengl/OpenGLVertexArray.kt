package hazel.renderer.opengl

import com.kgl.opengl.*
import hazel.core.*
import hazel.opengl.*
import hazel.renderer.*
import kotlinx.cinterop.*

internal class OpenGLVertexArray : VertexArray {
	private val rendererID: UInt

	override val vertexBuffers = mutableListOf<VertexBuffer>()

	private lateinit var _indexBuffer: IndexBuffer
	override var indexBuffer: IndexBuffer
		get() = _indexBuffer
		set(value) {
			Hazel.profile("OpenGLVertexArray.indexBuffer.set(IndexBuffer)") {
				glBindVertexArray(rendererID)
				value.bind()
				_indexBuffer = value
			}
		}

	init {
		val profiler = Hazel.Profiler("OpenGLVertexArray(): OpenGLVertexBuffer")
		profiler.start()

		rendererID = glGenVertexArray()

		profiler.stop()
	}

	override fun dispose() {
		Hazel.profile("OpenGLVertexArray.dispose()") {
			vertexBuffers.forEach { it.dispose() }
			indexBuffer.dispose()
			glDeleteVertexArrays(rendererID)
		}
	}

	override fun bind() {
		Hazel.profile("OpenGLVertexArray.bind()") {
			glBindVertexArray(rendererID)
		}
	}

	override fun unbind() {
		Hazel.profile("OpenGLVertexArray.unbind()") {
			glBindVertexArray(0u)
		}
	}

	override fun addVertexBuffer(vertexBuffer: VertexBuffer) {
		Hazel.profile("OpenGLVertexArray.addVertexBuffer(VertexBuffer)") {
			Hazel.coreAssert(vertexBuffer.layout.elements.isNotEmpty(), "Vertex buffer has no layout!")

			glBindVertexArray(rendererID)
			vertexBuffer.bind()

			vertexBuffer.layout.elements.forEachIndexed { index, element ->
				when (element.type) {
					ShaderDataType.Float,
					ShaderDataType.Float2,
					ShaderDataType.Float3,
					ShaderDataType.Float4,
					ShaderDataType.Int,
					ShaderDataType.Int2,
					ShaderDataType.Int3,
					ShaderDataType.Int4,
					ShaderDataType.Boolean -> {
						glEnableVertexAttribArray(index.toUInt())
						glVertexAttribPointer(
							index.toUInt(),
							element.componentCount,
							element.type.toOpenGLBaseType(),
							element.isNormalized,
							vertexBuffer.layout.stride,
							element.offset
						)
					}
					ShaderDataType.Mat3,
					ShaderDataType.Mat4 -> {
						val count = element.componentCount
						for (i in 0 until count) {
							glEnableVertexAttribArray(index.toUInt())
							glVertexAttribPointer(
								index.toUInt(),
								count,
								element.type.toOpenGLBaseType(),
								element.isNormalized,
								vertexBuffer.layout.stride,
								(element.offset + sizeOf<FloatVar>() * count * i).toInt()
							)
							glVertexAttribDivisor(index.toUInt(), 1u)
						}
					}
				}
			}

			vertexBuffers.add(vertexBuffer)
		}
	}


	private fun ShaderDataType.toOpenGLBaseType() = when (this) {
		ShaderDataType.Boolean -> GL_BOOL
		ShaderDataType.Int -> GL_INT
		ShaderDataType.Int2 -> GL_INT
		ShaderDataType.Int3 -> GL_INT
		ShaderDataType.Int4 -> GL_INT
		ShaderDataType.Float -> GL_FLOAT
		ShaderDataType.Float2 -> GL_FLOAT
		ShaderDataType.Float3 -> GL_FLOAT
		ShaderDataType.Float4 -> GL_FLOAT
		ShaderDataType.Mat3 -> GL_FLOAT
		ShaderDataType.Mat4 -> GL_FLOAT
	}
}
