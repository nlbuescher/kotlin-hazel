package hazel.renderer.opengl

import copengl.GL_ARRAY_BUFFER
import copengl.GL_ELEMENT_ARRAY_BUFFER
import copengl.GL_STATIC_DRAW
import hazel.core.Hazel
import hazel.core.profile
import hazel.renderer.BufferLayout
import hazel.renderer.IndexBuffer
import hazel.renderer.VertexBuffer
import opengl.glBindBuffer
import opengl.glBufferData
import opengl.glCreateBuffer
import opengl.glDeleteBuffer

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

class OpenGLIndexBuffer(indices: UIntArray) : IndexBuffer {

    override val count: Int = indices.size

    private val rendererId: UInt

    init {
        val profiler = Hazel.Profiler(::OpenGLIndexBuffer)
        profiler.start()

        rendererId = glCreateBuffer()
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, rendererId)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW)

        profiler.stop()
    }

    override fun dispose() {
        Hazel.profile(::dispose) {
            glDeleteBuffer(rendererId)
        }
    }

    override fun bind() {
        Hazel.profile(::bind) {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, rendererId)
        }
    }

    override fun unbind() {
        Hazel.profile(::unbind) {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0u)
        }
    }
}
