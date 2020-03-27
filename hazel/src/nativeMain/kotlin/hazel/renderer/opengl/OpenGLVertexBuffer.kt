package hazel.renderer.opengl

import com.kgl.opengl.*
import hazel.core.Hazel
import hazel.core.profile
import hazel.opengl.glBufferData
import hazel.renderer.BufferLayout
import hazel.renderer.VertexBuffer

internal class OpenGLVertexBuffer(vertices: FloatArray) : VertexBuffer {

	private val rendererId: UInt

	override var layout = BufferLayout()

	init {
		val profiler = Hazel.Profiler(::OpenGLVertexBuffer)
		profiler.start()

		rendererId = glGenBuffer()
		glBindBuffer(GL_ARRAY_BUFFER, rendererId)
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)

		profiler.stop()
	}

	override fun dispose() {
		Hazel.profile(::dispose) {
			glDeleteBuffer(rendererId)
		}
	}

	override fun bind() {
		Hazel.profile(::bind) {
			glBindBuffer(GL_ARRAY_BUFFER, rendererId)
		}
	}

	override fun unbind() {
		Hazel.profile(::unbind) {
			glBindBuffer(GL_ARRAY_BUFFER, 0u)
		}
	}
}
