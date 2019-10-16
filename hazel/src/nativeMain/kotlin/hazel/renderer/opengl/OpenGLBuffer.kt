package hazel.renderer.opengl

import copengl.GL_ARRAY_BUFFER
import copengl.GL_ELEMENT_ARRAY_BUFFER
import copengl.GL_STATIC_DRAW
import hazel.renderer.BufferLayout
import hazel.renderer.IndexBuffer
import hazel.renderer.VertexBuffer
import opengl.glBindBuffer
import opengl.glBufferData
import opengl.glCreateBuffer
import opengl.glDeleteBuffer

class OpenGLVertexBuffer(vertices: FloatArray) : VertexBuffer {
    private val rendererId: UInt = glCreateBuffer()

    init {
        glBindBuffer(GL_ARRAY_BUFFER, rendererId)
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)
    }

    override var layout = BufferLayout()

    override fun bind() {
        glBindBuffer(GL_ARRAY_BUFFER, rendererId)
    }

    override fun unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0u)
    }

    override fun dispose() {
        glDeleteBuffer(rendererId)
    }
}

class OpenGLIndexBuffer(indices: UIntArray) : IndexBuffer {

    override val count: Int = indices.size

    private val rendererId: UInt = glCreateBuffer()

    init {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, rendererId)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW)
    }

    override fun bind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, rendererId)
    }

    override fun unbind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0u)
    }

    override fun dispose() {
        glDeleteBuffer(rendererId)
    }
}
