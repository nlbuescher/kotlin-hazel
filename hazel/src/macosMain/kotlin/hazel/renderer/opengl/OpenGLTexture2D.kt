package hazel.renderer.opengl

import com.kgl.opengl.*
import cstb.sizeOf
import cstb.stbi_image_free
import cstb.stbi_load
import cstb.stbi_set_flip_vertically_on_load
import hazel.core.Hazel
import hazel.core.coreAssert
import hazel.core.profile
import hazel.opengl.glDeleteTextures
import hazel.opengl.glTexImage2D
import hazel.opengl.glTexParameter
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
		val profiler = Hazel.Profiler("${this::class.qualifiedName}.<init>(${Int::class.qualifiedName},${Int::class.qualifiedName})${this::class.qualifiedName}")
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
		val profiler = Hazel.Profiler("${this::class.qualifiedName}.<init>(${String::class.qualifiedName})${this::class.qualifiedName}")
		profiler.start()

		this.path = path

		val meta = IntArray(3)
		val data: ByteArray? = meta.usePinned { pinned ->
			stbi_set_flip_vertically_on_load(1)

			Hazel.profile("stbi_load - ${this::class.qualifiedName}.<init>(${String::class.qualifiedName})${this::class.qualifiedName}") {
				stbi_load(path, pinned.addressOf(0), pinned.addressOf(1), pinned.addressOf(2), 0)?.let { pointer ->
					val size = sizeOf(pointer).toInt()
					ByteArray(size).apply {
						usePinned { memcpy(it.addressOf(0), pointer, size.toULong()) }
						stbi_image_free(pointer)
					}
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

		rendererId = glGenTexture()
		glBindTexture(GL_TEXTURE_2D, rendererId)

		glTexParameter(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
		glTexParameter(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)

		glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, dataFormat, data)

		profiler.stop()
	}

	override fun dispose() {
		Hazel.profile(::dispose) {
			glDeleteTextures(rendererId)
		}
	}

	override fun setData(data: ByteArray) {
		Hazel.profile(::setData) {
			val bpp = if (dataFormat == GL_RGBA) 4 else 3
			Hazel.coreAssert(data.size == width * height * bpp) { "Data must be entire texture" }
			glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, dataFormat, data)
		}
	}

	override fun bind(slot: UInt) {
		Hazel.profile(::bind) {
			glActiveTexture(GL_TEXTURE0 + slot)
			glBindTexture(GL_TEXTURE_2D, rendererId)
		}
	}
}
