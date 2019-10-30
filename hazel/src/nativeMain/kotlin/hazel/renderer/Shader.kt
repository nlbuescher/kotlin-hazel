package hazel.renderer

import hazel.core.Disposable
import hazel.renderer.opengl.OpenGLShader

interface Shader : Disposable {
    fun bind()
    fun unbind()
}

fun Shader(filepath: String) = when (Renderer.api) {
    RenderAPI.API.None -> TODO("RenderAPI.API.None is currently not supported")
    RenderAPI.API.OpenGL -> OpenGLShader(filepath)
}

fun Shader(vertexSource: String, fragmentSource: String) = when (Renderer.api) {
    RenderAPI.API.None -> TODO("RenderAPI.API.None is currently not supported")
    RenderAPI.API.OpenGL -> OpenGLShader(vertexSource, fragmentSource)
}
