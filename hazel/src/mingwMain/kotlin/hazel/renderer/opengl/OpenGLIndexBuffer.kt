package hazel.renderer.opengl

import com.kgl.opengl.GL_ELEMENT_ARRAY_BUFFER
import com.kgl.opengl.GL_STATIC_DRAW
import com.kgl.opengl.glBindBuffer
import com.kgl.opengl.glCreateBuffer
import hazel.core.Hazel
import hazel.core.profile
import hazel.opengl.glBufferData
import hazel.opengl.glDeleteBuffers
import hazel.renderer.IndexBuffer

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
            glDeleteBuffers(rendererId)
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
