package hazel.renderer

import hazel.Disposable
import hazel.renderer.RenderAPI.None
import hazel.renderer.RenderAPI.OpenGL

interface VertexBuffer : Disposable {
    fun bind()
    fun unbind()
}

fun vertexBufferOf(vararg vertices: Float): VertexBuffer {
    return when (Renderer.renderAPI) {
        None -> TODO("RenderAPI.None is currently not supported")
        OpenGL -> OpenGLVertexBuffer(vertices)
    }
}

interface IndexBuffer : Disposable {
    val count: Int
    fun bind()
    fun unbind()
}

fun indexBufferOf(vararg indices: UInt): IndexBuffer {
    return when (Renderer.renderAPI) {
        None -> TODO("RenderAPI.None is currently not supported")
        OpenGL -> OpenGLIndexBuffer(indices)
    }
}
