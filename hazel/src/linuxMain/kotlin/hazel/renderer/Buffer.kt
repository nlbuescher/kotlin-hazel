package hazel.renderer

actual fun vertexBufferOf(vararg vertices: Float): VertexBuffer {
    return when (Renderer.renderAPI) {
        RenderAPI.None -> TODO("RenderAPI.None is currently not supported")
        RenderAPI.OpenGL -> OpenGLVertexBuffer(vertices)
    }
}

actual fun indexBufferOf(vararg indices: UInt): IndexBuffer {
    return when (Renderer.renderAPI) {
        RenderAPI.None -> TODO("RenderAPI.None is currently not supported")
        RenderAPI.OpenGL -> OpenGLIndexBuffer(indices)
    }
}
