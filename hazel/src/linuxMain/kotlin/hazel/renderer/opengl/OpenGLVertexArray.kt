package hazel.renderer.opengl

import com.kgl.opengl.GL_BOOL
import com.kgl.opengl.GL_FLOAT
import com.kgl.opengl.GL_INT
import com.kgl.opengl.glBindVertexArray
import com.kgl.opengl.glCreateVertexArray
import com.kgl.opengl.glEnableVertexAttribArray
import hazel.core.Hazel
import hazel.opengl.glDeleteVertexArrays
import hazel.opengl.glVertexAttribPointer
import hazel.renderer.IndexBuffer
import hazel.renderer.ShaderDataType
import hazel.renderer.VertexArray
import hazel.renderer.VertexBuffer

private fun ShaderDataType.toOpenGLBaseType(): UInt = when (this) {
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

internal class OpenGLVertexArray : VertexArray {

    private val rendererId: UInt = glCreateVertexArray()

    override val vertexBuffers = mutableListOf<VertexBuffer>()

    private lateinit var _indexBuffer: IndexBuffer
    override var indexBuffer: IndexBuffer
        get() = _indexBuffer
        set(indexBuffer) {
            glBindVertexArray(rendererId)
            indexBuffer.bind()
            _indexBuffer = indexBuffer
        }


    override fun bind() {
        glBindVertexArray(rendererId)
    }

    override fun unbind() {
        glBindVertexArray(0u)
    }

    override fun addVertexBuffer(vertexBuffer: VertexBuffer) {
        Hazel.coreAssert(vertexBuffer.layout.elements.isNotEmpty()) { "Vertex buffer has no layout!" }

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

    override fun dispose() {
        vertexBuffers.forEach { it.dispose() }
        indexBuffer.dispose()
        glDeleteVertexArrays(rendererId)
    }
}
