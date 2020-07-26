package hazel.renderer

import hazel.renderer.opengl.*

//TODO replace [FrameBuffer.Specification] with builder pattern

interface FrameBuffer {
	val specification: Specification

	val colorAttachmentRendererID: UInt

	fun bind()
	fun unbind()

	fun resize(width: Int, height: Int)

	class Specification(
		val width: Int,
		val height: Int,
		val samples: Int = 1,

		val isSwapChainTarget: Boolean = false
	) {
		fun copy(
			width: Int = this.width,
			height: Int = this.height,
			samples: Int = this.samples,
			isSwapChainTarget: Boolean = this.isSwapChainTarget
		) = Specification(width, height, samples, isSwapChainTarget)
	}
}

@Suppress("FunctionName")
fun FrameBuffer(specification: FrameBuffer.Specification): FrameBuffer = when (Renderer.api) {
	RenderAPI.API.None -> TODO("RenderAPI.API.None is currently not supported")
	RenderAPI.API.OpenGL -> OpenGLFrameBuffer(specification)
}
