package hazel.renderer.opengl

import com.kgl.opengl.*
import hazel.core.*
import hazel.math.Vec4
import hazel.renderer.RenderAPI
import hazel.renderer.VertexArray
import kotlinx.cinterop.staticCFunction
import kotlinx.cinterop.toKString

class OpenGLRenderAPI : RenderAPI {
	override fun init() {
		if (Platform.isDebugBinary) {
			glEnable(GL_DEBUG_OUTPUT)
			glEnable(GL_DEBUG_OUTPUT_SYNCHRONOUS)
			glDebugMessageCallback(staticCFunction { _, _, _, severity, _, message, _ ->
				val errorMessage = message?.toKString() ?: ""
				when (severity) {
					GL_DEBUG_SEVERITY_HIGH -> Hazel.coreCritical(errorMessage)
					GL_DEBUG_SEVERITY_MEDIUM -> Hazel.coreError(errorMessage)
					GL_DEBUG_SEVERITY_LOW -> Hazel.coreWarn(errorMessage)
					GL_DEBUG_SEVERITY_NOTIFICATION -> Hazel.coreTrace(errorMessage)
				}
			}, null)

			glDebugMessageControl(GL_DONT_CARE, GL_DONT_CARE, GL_DEBUG_SEVERITY_NOTIFICATION, 0, null, false)
		}

		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

		glEnable(GL_DEPTH_TEST)
	}

	override fun setViewport(x: Int, y: Int, width: Int, height: Int) {
		glViewport(x, y, width, height)
	}

	override fun setClearColor(color: Vec4) {
		glClearColor(color.x, color.y, color.z, color.w)
	}

	override fun clear() {
		glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
	}

	override fun drawIndexed(vertexArray: VertexArray, indexCount: Int) {
		val count = if (indexCount == 0) vertexArray.indexBuffer.count else indexCount
		glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_INT, null)
		glBindTexture(GL_TEXTURE_2D, 0u)
	}
}
