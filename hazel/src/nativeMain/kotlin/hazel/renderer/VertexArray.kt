package hazel.renderer

import hazel.Disposable

interface VertexArray : Disposable {
    fun bind()
    fun unbind()

    var indexBuffer: IndexBuffer
    val vertexBuffers: List<VertexBuffer>
    fun addVertexBuffer(vertexBuffer: VertexBuffer)
}

expect fun VertexArray(): VertexArray
