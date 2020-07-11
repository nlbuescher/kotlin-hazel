package hazel.renderer

import hazel.core.*
import hazel.renderer.opengl.*

interface Texture : Disposable {
	val rendererID: UInt
	val width: Int
	val height: Int

	fun setData(data: ByteArray)
	fun bind(slot: Int = 0)
}

interface Texture2D : Texture

@Suppress("FunctionName")
fun Texture2D(width: Int, height: Int) = when (Renderer.api) {
	RenderAPI.API.None -> TODO("RenderAPI.API.None is currently not supported")
	RenderAPI.API.OpenGL -> OpenGLTexture2D(width, height)
}

@Suppress("FunctionName")
fun Texture2D(path: String): Texture2D = when (Renderer.api) {
	RenderAPI.API.None -> TODO("RenderAPI.API.None is currently not supported")
	RenderAPI.API.OpenGL -> OpenGLTexture2D(path)
}
