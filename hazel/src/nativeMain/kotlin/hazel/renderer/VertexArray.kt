package hazel.renderer

import hazel.Disposable
import hazel.renderer.opengl.OpenGLVertexArray

interface VertexArray : Disposable {
    fun bind()
    fun unbind()

    var indexBuffer: IndexBuffer
    val vertexBuffers: List<VertexBuffer>
    fun addVertexBuffer(vertexBuffer: VertexBuffer)
}

fun VertexArray(): VertexArray {
    return when (Renderer.renderAPI) {
        RenderAPI.None -> TODO("RenderAPI.None is currently not supported")
        RenderAPI.OpenGL -> OpenGLVertexArray()
    }
}
