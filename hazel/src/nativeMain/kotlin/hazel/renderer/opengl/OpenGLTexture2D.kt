package hazel.renderer.opengl

import com.kgl.opengl.*
import hazel.cinterop.sizeOf
import hazel.cinterop.stbi_image_free
import hazel.cinterop.stbi_load
import hazel.cinterop.stbi_set_flip_vertically_on_load
import hazel.core.Hazel
import hazel.core.coreAssert
import hazel.core.profile
import hazel.opengl.glDeleteTextures
import hazel.opengl.glTexImage2D
import hazel.opengl.glTexParameter
import hazel.renderer.Texture2D
import kotlinx.cinterop.refTo
import platform.posix.memcpy

class OpenGLTexture2D : Texture2D {
	private val rendererId: UInt

	private val path: String?

	override val width: Int
	override val height: Int

	private val internalFormat: UInt
	private val dataFormat: UInt

	constructor(width: Int, height: Int) {
		val profiler = Hazel.Profiler("OpenGLTexture2D(Int, Int)")
		profiler.start()

		this.path = null
		this.width = width
		this.height = height

		internalFormat = GL_RGBA8
		dataFormat = GL_RGBA

		rendererId = glGenTexture()
		glBindTexture(GL_TEXTURE_2D, rendererId)

		glTexParameter(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
		glTexParameter(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)

		profiler.stop()
	}

	constructor(path: String) {
		val profiler = Hazel.Profiler("OpenGLTexture2D(String): OpenGLTexture2D")
		profiler.start()

		this.path = path

		stbi_set_flip_vertically_on_load(1)

		val meta = IntArray(3)
		val data: ByteArray? = Hazel.profile("stbi_load(String, ...): ByteArray") {
			stbi_load(path, meta.refTo(0), meta.refTo(1), meta.refTo(2), 0)?.let { pointer ->
				val size = sizeOf(pointer).toInt()
				ByteArray(size).apply {
					memcpy(refTo(0), pointer, size.toULong())
					stbi_image_free(pointer)
				}
			}
		}
		Hazel.coreAssert(data != null, "Failed to load image!")

		width = meta[0]; height = meta[1]

		val (ifmt, dfmt) = when (meta[2]) {
			4 -> GL_RGBA8 to GL_RGBA
			3 -> GL_RGB8 to GL_RGB
			else -> 0u to 0u
		}

		internalFormat = ifmt
		dataFormat = dfmt

		Hazel.coreAssert(internalFormat != 0u && dataFormat != 0u, "Format not supported!")

		rendererId = glGenTexture()
		glBindTexture(GL_TEXTURE_2D, rendererId)

		glTexParameter(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
		glTexParameter(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)

		glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, dataFormat, data)

		profiler.stop()
	}

	override fun dispose() {
		Hazel.profile("OpenGLTexture2D.dispose()") {
			glDeleteTextures(rendererId)
		}
	}

	override fun setData(data: ByteArray) {
		Hazel.profile("OpenGLTexture2D.setData(ByteArray)") {
			val bpp = if (dataFormat == GL_RGBA) 4 else 3
			Hazel.coreAssert(data.size == width * height * bpp, "Data must be entire texture")
			glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, dataFormat, data)
		}
	}

	override fun bind(slot: UInt) {
		Hazel.profile("OpenGLTexture2D.bind(UInt)") {
			glActiveTexture(GL_TEXTURE0 + slot)
			glBindTexture(GL_TEXTURE_2D, rendererId)
		}
	}
}
