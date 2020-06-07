package hazel.renderer

import hazel.core.*
import hazel.math.*
import hazel.renderer.opengl.*

interface Shader : Disposable {
	val name: String

	fun bind()
	fun unbind()

	operator fun set(name: String, int: Int)
	operator fun set(name: String, ints: IntArray)
	operator fun set(name: String, float: Float)
	operator fun set(name: String, vector: Vec3)
	operator fun set(name: String, vector: Vec4)
	operator fun set(name: String, matrix: Mat4)
}

@Suppress("FunctionName")
fun Shader(filepath: String): Shader = when (Renderer.api) {
	RenderAPI.API.None -> TODO("RenderAPI.API.None is currently not supported")
	RenderAPI.API.OpenGL -> OpenGLShader(filepath)
}

@Suppress("FunctionName")
fun Shader(name: String, vertexSource: String, fragmentSource: String): Shader = when (Renderer.api) {
	RenderAPI.API.None -> TODO("RenderAPI.API.None is currently not supported")
	RenderAPI.API.OpenGL -> OpenGLShader(name, vertexSource, fragmentSource)
}


class ShaderLibrary : Disposable {
	private val shaders = mutableMapOf<String, Shader>()

	override fun dispose() = shaders.values.forEach { it.dispose() }

	fun add(shader: Shader) = add(shader.name, shader)

	fun add(name: String, shader: Shader) {
		Hazel.coreAssert(name !in this, "Shader '$name' already exists!")
		shaders[name] = shader
	}

	fun load(filepath: String) = Shader(filepath).also { add(it) }

	fun load(name: String, filepath: String) = Shader(filepath).also { add(name, it) }

	operator fun get(name: String): Shader {
		Hazel.coreAssert(name in this, "Shader '$name' not found!")
		return shaders[name]!!
	}

	operator fun contains(name: String): Boolean = name in shaders.keys
}
