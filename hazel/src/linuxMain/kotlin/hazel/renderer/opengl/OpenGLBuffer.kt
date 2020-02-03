package hazel.renderer.opengl

import com.kgl.opengl.GL_ARRAY_BUFFER
import com.kgl.opengl.GL_ELEMENT_ARRAY_BUFFER
import com.kgl.opengl.GL_STATIC_DRAW
import com.kgl.opengl.glBindBuffer
import com.kgl.opengl.glCreateBuffer
import hazel.opengl.glBufferData
import hazel.opengl.glDeleteBuffers
import hazel.renderer.BufferLayout
import hazel.renderer.IndexBuffer
import hazel.renderer.VertexBuffer

internal class OpenGLVertexBuffer(vertices: FloatArray) : VertexBuffer {
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
        glDeleteBuffers(rendererId)
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
        glDeleteBuffers(rendererId)
    }
}
