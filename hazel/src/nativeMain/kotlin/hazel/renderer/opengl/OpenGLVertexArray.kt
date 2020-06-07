package hazel.renderer.opengl

import com.kgl.opengl.*
import hazel.core.*
import hazel.opengl.*
import hazel.renderer.*

internal class OpenGLVertexArray : VertexArray {
	private val rendererId: UInt

	override val vertexBuffers = mutableListOf<VertexBuffer>()

	private lateinit var _indexBuffer: IndexBuffer
	override var indexBuffer: IndexBuffer
		get() = _indexBuffer
		set(value) {
			Hazel.profile("OpenGLVertexArray.indexBuffer.set(IndexBuffer)") {
				glBindVertexArray(rendererId)
				value.bind()
				_indexBuffer = value
			}
		}

	init {
		val profiler = Hazel.Profiler("OpenGLVertexArray(): OpenGLVertexBuffer")
		profiler.start()

		rendererId = glGenVertexArray()

		profiler.stop()
	}

	override fun dispose() {
		Hazel.profile("OpenGLVertexArray.dispose()") {
			vertexBuffers.forEach { it.dispose() }
			indexBuffer.dispose()
			glDeleteVertexArrays(rendererId)
		}
	}

	override fun bind() {
		Hazel.profile("OpenGLVertexArray.bind()") {
			glBindVertexArray(rendererId)
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

			glBindVertexArray(rendererId)
			vertexBuffer.bind()

			vertexBuffer.layout.elements.forEachIndexed { index, element ->
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
