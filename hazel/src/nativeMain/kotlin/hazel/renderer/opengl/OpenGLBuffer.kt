package hazel.renderer.opengl

import com.kgl.opengl.*
import hazel.core.Hazel
import hazel.core.profile
import hazel.opengl.glBufferData
import hazel.renderer.BufferLayout
import hazel.renderer.IndexBuffer
import hazel.renderer.VertexBuffer

internal class OpenGLVertexBuffer(vertices: FloatArray) : VertexBuffer {
	private val rendererId: UInt

	override var layout = BufferLayout()

	init {
		val profiler = Hazel.Profiler("OpenGLVertexBuffer(): OpenGLVertexBuffer")
		profiler.start()

		rendererId = glGenBuffer()
		glBindBuffer(GL_ARRAY_BUFFER, rendererId)
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)

		profiler.stop()
	}

	override fun dispose() {
		Hazel.profile("OpenGLVertexBuffer.dispose()") {
			glDeleteBuffer(rendererId)
		}
	}

	override fun bind() {
		Hazel.profile("OpenGLVertexBuffer.bind()") {
			glBindBuffer(GL_ARRAY_BUFFER, rendererId)
		}
	}

	override fun unbind() {
		Hazel.profile("OpenGLVertexBuffer.unbind()") {
			glBindBuffer(GL_ARRAY_BUFFER, 0u)
		}
	}
}

class OpenGLIndexBuffer(indices: UIntArray) : IndexBuffer {
	private val rendererId: UInt

	override val count: Int = indices.size

	init {
		val profiler = Hazel.Profiler("OpenGLIndexBuffer(UIntArray): OpenGLIndexBuffer")
		profiler.start()

		rendererId = glGenBuffer()
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, rendererId)
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW)

		profiler.stop()
	}

	override fun dispose() {
		Hazel.profile("OpenGLIndexBuffer.dispose()") {
			glDeleteBuffer(rendererId)
		}
	}

	override fun bind() {
		Hazel.profile("OpenGLIndexBuffer.bind()") {
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, rendererId)
		}
	}

	override fun unbind() {
		Hazel.profile("OpenGLIndexBuffer.unbind()") {
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0u)
		}
	}
}
