package hazel.renderer.opengl

import com.kgl.opengl.*
import hazel.core.*
import hazel.renderer.*

class OpenGLFrameBuffer(
	override val specification: FrameBuffer.Specification
) : FrameBuffer, Disposable {
	private var rendererID: UInt = 0u

	private var colorAttachment: UInt = 0u
	override val colorAttachmentRendererID: UInt get() = colorAttachment

	private var depthAttachment: UInt = 0u

	init {
		invalidate()
	}

	override fun dispose() {
		glDeleteFramebuffer(rendererID)
	}

	private fun invalidate() {
		rendererID = glGenFramebuffer()
		glBindFramebuffer(GL_FRAMEBUFFER, rendererID)

		//@formatter:off
		colorAttachment = glCreateTexture(GL_TEXTURE_2D)
		glBindTexture(GL_TEXTURE_2D, colorAttachment)
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8.toInt(), specification.width, specification.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, null)
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR.toInt())
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR.toInt())
		//@formatter:on

		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, colorAttachment, 0)

		//@formatter:off
		depthAttachment = glCreateTexture(GL_TEXTURE_2D)
		glBindTexture(GL_TEXTURE_2D, depthAttachment)
		glTexStorage2D(GL_TEXTURE_2D, 1, GL_DEPTH24_STENCIL8, specification.width, specification.height)
//		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH24_STENCIL8.toInt(), specification.width, specification.height, 0, GL_DEPTH_STENCIL, GL_UNSIGNED_INT_24_8, null)
		//@formatter:on

		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_TEXTURE_2D, depthAttachment, 0)

		Hazel.coreAssert(
			glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE,
			"Frame buffer is incomplete!"
		)

		glBindFramebuffer(GL_FRAMEBUFFER, 0u)
	}

	override fun bind() {
		glBindFramebuffer(GL_FRAMEBUFFER, rendererID)
	}

	override fun unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0u)
	}
}
