package hazel.renderer

import hazel.core.Disposable
import hazel.core.Hazel
import hazel.core.coreAssert
import hazel.math.Mat4
import hazel.math.Vec3
import hazel.math.Vec4
import hazel.renderer.opengl.OpenGLShader

interface Shader : Disposable {
	val name: String

	fun bind()
	fun unbind()

	operator fun set(name: String, int: Int)
	operator fun set(name: String, float: Float)
	operator fun set(name: String, vector: Vec3)
	operator fun set(name: String, vector: Vec4)
	operator fun set(name: String, matrix: Mat4)
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
