package hazel.renderer.opengl

import com.kgl.opengl.GL_LINEAR
import com.kgl.opengl.GL_NEAREST
import com.kgl.opengl.GL_RGB
import com.kgl.opengl.GL_RGB8
import com.kgl.opengl.GL_RGBA
import com.kgl.opengl.GL_RGBA8
import com.kgl.opengl.GL_TEXTURE_2D
import com.kgl.opengl.GL_TEXTURE_MAG_FILTER
import com.kgl.opengl.GL_TEXTURE_MIN_FILTER
import com.kgl.opengl.GL_UNSIGNED_BYTE
import com.kgl.opengl.glBindTextureUnit
import com.kgl.opengl.glCreateTexture
import com.kgl.opengl.glTextureStorage2D
import cstb_image.sizeOf
import cstb_image.stbi_image_free
import cstb_image.stbi_load
import cstb_image.stbi_set_flip_vertically_on_load
import hazel.core.Hazel
import hazel.opengl.glDeleteTextures
import hazel.opengl.glTextureParameter
import hazel.opengl.glTextureSubImage2D
import hazel.renderer.Texture2D
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.posix.memcpy

class OpenGLTexture2D : Texture2D {
    private val path: String?

    override val width: Int
    override val height: Int

    private val internalFormat: UInt
    private val dataFormat: UInt

    private val rendererId: UInt


    constructor(width: Int, height: Int) {
        this.path = null
        this.width = width
        this.height = height

        internalFormat = GL_RGBA8
        dataFormat = GL_RGBA

        rendererId = glCreateTexture(GL_TEXTURE_2D)
        glTextureStorage2D(rendererId, 1, internalFormat, width, height)

        glTextureParameter(rendererId, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTextureParameter(rendererId, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
    }

    constructor(path: String) {
        this.path = path

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

        val (ifmt, dfmt) = when (meta[2]) {
            4 -> GL_RGBA8 to GL_RGBA
            3 -> GL_RGB8 to GL_RGB
            else -> 0u to 0u
        }

        internalFormat = ifmt
        dataFormat = dfmt

        Hazel.coreAssert(internalFormat != 0u && dataFormat != 0u) { "Format not supported!" }

        rendererId = glCreateTexture(GL_TEXTURE_2D)
        glTextureStorage2D(rendererId, 1, internalFormat, width, height)

        glTextureParameter(rendererId, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTextureParameter(rendererId, GL_TEXTURE_MAG_FILTER, GL_NEAREST)

        glTextureSubImage2D(rendererId, 0, 0, 0, width, height, dataFormat, GL_UNSIGNED_BYTE, data)
    }


    override fun setData(data: ByteArray) {
        val bpp = if (dataFormat == GL_RGBA) 4 else 3
        Hazel.coreAssert(data.size == width * height * bpp) { "Data must be entire texture" }
        glTextureSubImage2D(rendererId, 0, 0, 0, width, height, dataFormat, GL_UNSIGNED_BYTE, data)
    }

    override fun bind(slot: UInt) {
        glBindTextureUnit(slot, rendererId)
    }

    override fun dispose() {
        glDeleteTextures(rendererId)
    }
}
