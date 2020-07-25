package hazel.renderer

import hazel.renderer.opengl.*

//TODO replace [FrameBuffer.Specification] with builder pattern

interface FrameBuffer {
	val specification: Specification

	val colorAttachmentRendererID: UInt

	fun bind()
	fun unbind()

	class Specification(
		val width: Int,
		val height: Int,
		val samples: Int = 1,

		val isSwapChainTarget: Boolean = false
	)
}

@Suppress("FunctionName")
fun FrameBuffer(specification: FrameBuffer.Specification): FrameBuffer = when (Renderer.api) {
	RenderAPI.API.None -> TODO("RenderAPI.API.None is currently not supported")
	RenderAPI.API.OpenGL -> OpenGLFrameBuffer(specification)
}
