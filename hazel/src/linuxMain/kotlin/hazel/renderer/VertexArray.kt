package hazel.renderer

import hazel.renderer.opengl.OpenGLVertexArray

actual fun VertexArray(): VertexArray {
    return when (Renderer.renderAPI) {
        RenderAPI.None -> TODO("RenderAPI.None is currently not supported")
        RenderAPI.OpenGL -> OpenGLVertexArray()
    }
}
