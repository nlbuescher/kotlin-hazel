package hazel.renderer.opengl

import com.kgl.opengl.GL_ARRAY_BUFFER
import com.kgl.opengl.GL_STATIC_DRAW
import com.kgl.opengl.glBindBuffer
import com.kgl.opengl.glCreateBuffer
import hazel.core.Hazel
import hazel.core.profile
import hazel.opengl.glBufferData
import hazel.opengl.glDeleteBuffers
import hazel.renderer.BufferLayout
import hazel.renderer.VertexBuffer

internal class OpenGLVertexBuffer(vertices: FloatArray) : VertexBuffer {
    private val rendererId: UInt

    override var layout = BufferLayout()

    init {
        val profiler = Hazel.Profiler(::OpenGLVertexBuffer)
        profiler.start()

        rendererId = glCreateBuffer()
        glBindBuffer(GL_ARRAY_BUFFER, rendererId)
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)

        profiler.stop()
    }

    override fun dispose() {
        Hazel.profile(::dispose) {
            glDeleteBuffers(rendererId)
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