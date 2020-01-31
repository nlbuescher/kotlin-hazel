package hazel.renderer

import hazel.core.Disposable
import hazel.core.Hazel
import hazel.core.coreAssert
import hazel.math.FloatMatrix4x4
import hazel.math.FloatVector3
import hazel.math.FloatVector4
import hazel.renderer.opengl.OpenGLShader

interface Shader : Disposable {
    val name: String

    fun bind()
    fun unbind()

    operator fun set(name: String, int: Int)
    operator fun set(name: String, float: Float)
    operator fun set(name: String, vector: FloatVector3)
    operator fun set(name: String, vector: FloatVector4)
    operator fun set(name: String, matrix: FloatMatrix4x4)
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

    override fun dispose() = shaders.forEach { (_, it) -> it.dispose() }

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
}
