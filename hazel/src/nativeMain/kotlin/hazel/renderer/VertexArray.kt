package hazel.renderer

import hazel.core.Disposable
import hazel.renderer.opengl.OpenGLVertexArray

interface VertexArray : Disposable {
	fun bind()
	fun unbind()

	var indexBuffer: IndexBuffer
	val vertexBuffers: List<VertexBuffer>
	fun addVertexBuffer(vertexBuffer: VertexBuffer)
}

@Suppress("FunctionName")
fun VertexArray(): VertexArray = when (Renderer.api) {
	RenderAPI.API.None -> TODO("RenderAPI.API.None is currently not supported")
	RenderAPI.API.OpenGL -> OpenGLVertexArray()
}
