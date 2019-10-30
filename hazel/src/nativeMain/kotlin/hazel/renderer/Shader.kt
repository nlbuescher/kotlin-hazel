package hazel.renderer

import hazel.Hazel
import hazel.core.Disposable
import hazel.renderer.opengl.OpenGLShader

interface Shader : Disposable {
    val name: String

    fun bind()
    fun unbind()
}

fun Shader(filepath: String): Shader = when (Renderer.api) {
    RenderAPI.API.None -> TODO("RenderAPI.API.None is currently not supported")
    RenderAPI.API.OpenGL -> OpenGLShader(filepath)
}

fun Shader(name: String, vertexSource: String, fragmentSource: String): Shader = when (Renderer.api) {
    RenderAPI.API.None -> TODO("RenderAPI.API.None is currently not supported")
    RenderAPI.API.OpenGL -> OpenGLShader(name, vertexSource, fragmentSource)
}

class ShaderLibrary : Disposable {
    private val shaders = mutableMapOf<String, Shader>()

    fun add(shader: Shader) = add(shader.name, shader)

    fun add(name: String, shader: Shader) {
        Hazel.coreAssert(name !in shaders.keys) { "Shader already exists!" }
        shaders[name] = shader
    }

    fun load(filepath: String) = Shader(filepath).also { add(it) }

    fun load(name: String, filepath: String) = Shader(filepath).also { add(name, it) }

    operator fun get(name: String): Shader {
        Hazel.coreAssert(name in shaders.keys)
        return shaders[name]!!
    }

    override fun dispose() = shaders.forEach { (_, it) -> it.dispose() }
}
