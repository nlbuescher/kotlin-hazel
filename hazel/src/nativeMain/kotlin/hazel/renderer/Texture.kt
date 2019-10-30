package hazel.renderer

import hazel.core.Disposable
import hazel.renderer.opengl.OpenGLTexture2D

interface Texture : Disposable {
    val width: Int
    val height: Int

    fun bind(slot: Int = 0)
}

interface Texture2D : Texture

fun Texture2D(path: String): Texture2D = when (Renderer.api) {
    RenderAPI.API.None -> TODO("RenderAPI.API.None is currently not supported")
    RenderAPI.API.OpenGL -> OpenGLTexture2D(path)
}

