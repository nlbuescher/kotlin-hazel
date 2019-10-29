package hazel.renderer.opengl

import copengl.GL_LINEAR
import copengl.GL_NEAREST
import copengl.GL_RGB
import copengl.GL_RGB8
import copengl.GL_RGBA
import copengl.GL_RGBA8
import copengl.GL_TEXTURE_2D
import copengl.GL_TEXTURE_MAG_FILTER
import copengl.GL_TEXTURE_MIN_FILTER
import copengl.GL_UNSIGNED_BYTE
import cstb_image.sizeOf
import cstb_image.stbi_image_free
import cstb_image.stbi_load
import cstb_image.stbi_set_flip_vertically_on_load
import hazel.Hazel
import hazel.coreAssert
import hazel.renderer.Texture2D
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import opengl.glBindTextureUnit
import opengl.glCreateTexture
import opengl.glDeleteTextures
import opengl.glTextureParameter
import opengl.glTextureStorage2D
import opengl.glTextureSubImage2D
import platform.posix.memcpy

class OpenGLTexture2D(private val path: String) : Texture2D {
    override val width: Int
    override val height: Int

    private val rendererId: UInt

    init {
        val meta = IntArray(3)
        val data = meta.usePinned { pinned ->
            stbi_set_flip_vertically_on_load(1)
            stbi_load(path, pinned.addressOf(0), pinned.addressOf(1), pinned.addressOf(2), 0)?.let { pointer ->
                val size = sizeOf(pointer)
                ByteArray(size.toInt()).apply {
                    usePinned { memcpy(it.addressOf(0), pointer, size.toULong()) }
                    stbi_image_free(pointer)
                }
            }
        }
        Hazel.coreAssert(data != null) { "Failed to load image!" }

        width = meta[0]; height = meta[1]

        val (internalFormat, dataFormat) = when (meta[2]) {
            4 -> GL_RGBA8 to GL_RGBA
            3 -> GL_RGB8 to GL_RGB
            else -> 0 to 0
        }

        Hazel.coreAssert(internalFormat != 0 && dataFormat != 0) { "Format not supported!" }

        rendererId = glCreateTexture(GL_TEXTURE_2D)
        glTextureStorage2D(rendererId, 1, internalFormat, width, height)

        glTextureParameter(rendererId, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTextureParameter(rendererId, GL_TEXTURE_MAG_FILTER, GL_NEAREST)

        glTextureSubImage2D(rendererId, 0, 0, 0, width, height, dataFormat, GL_UNSIGNED_BYTE, data)
    }


    override fun bind(slot: Int) {
        glBindTextureUnit(slot, rendererId)
    }

    override fun dispose() {
        glDeleteTextures(rendererId)
    }
}