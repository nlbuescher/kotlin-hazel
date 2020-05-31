package hazel.renderer.opengl

import com.kgl.opengl.*
import hazel.math.Vec4
import hazel.renderer.RenderAPI
import hazel.renderer.VertexArray

class OpenGLRenderAPI : RenderAPI {

	override fun init() {
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

	override fun drawIndexed(vertexArray: VertexArray) {
		glDrawElements(GL_TRIANGLES, vertexArray.indexBuffer.count, GL_UNSIGNED_INT, null)
	}
}
