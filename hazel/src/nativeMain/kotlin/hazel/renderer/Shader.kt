package hazel.renderer

import hazel.Disposable
import hazel.renderer.opengl.OpenGLShader

interface Shader : Disposable {
    fun bind()
    fun unbind()
}

fun Shader(vertexSource: String, fragmentSource: String) = when (Renderer.api) {
    RenderAPI.API.None -> TODO("RenderAPI.API.None is currently not supported")
    RenderAPI.API.OpenGL -> OpenGLShader(vertexSource, fragmentSource)
}
