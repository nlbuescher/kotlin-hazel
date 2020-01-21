package hazel.renderer

import hazel.core.Disposable
import hazel.renderer.opengl.OpenGLTexture2D

interface Texture : Disposable {
    val width: Int
    val height: Int

    fun setData(data: ByteArray)
    fun bind(slot: UInt = 0u)
}

interface Texture2D : Texture

fun Texture2D(path: String): Texture2D = when (Renderer.api) {
    RenderAPI.API.None -> TODO("RenderAPI.API.None is currently not supported")
    RenderAPI.API.OpenGL -> OpenGLTexture2D(path)
}

fun Texture2D(width: Int, height: Int) = when (Renderer.api) {
    RenderAPI.API.None -> TODO("RenderAPI.API.None is currently not supported")
    RenderAPI.API.OpenGL -> OpenGLTexture2D(width, height)
}
