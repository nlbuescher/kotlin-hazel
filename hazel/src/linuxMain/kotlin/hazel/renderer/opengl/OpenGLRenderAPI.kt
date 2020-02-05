package hazel.renderer.opengl

import com.kgl.opengl.GL_BLEND
import com.kgl.opengl.GL_COLOR_BUFFER_BIT
import com.kgl.opengl.GL_DEPTH_BUFFER_BIT
import com.kgl.opengl.GL_DEPTH_TEST
import com.kgl.opengl.GL_ONE_MINUS_SRC_ALPHA
import com.kgl.opengl.GL_SRC_ALPHA
import com.kgl.opengl.GL_TRIANGLES
import com.kgl.opengl.GL_UNSIGNED_INT
import com.kgl.opengl.glBlendFunc
import com.kgl.opengl.glClear
import com.kgl.opengl.glClearColor
import com.kgl.opengl.glDrawElements
import com.kgl.opengl.glEnable
import com.kgl.opengl.glViewport
import hazel.math.FloatVector4
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

	override fun setClearColor(color: FloatVector4) {
		glClearColor(color.r, color.g, color.b, color.a)
	}

	override fun clear() {
		glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
	}

	override fun drawIndexed(vertexArray: VertexArray) {
		glDrawElements(GL_TRIANGLES, vertexArray.indexBuffer.count, GL_UNSIGNED_INT, null)
	}
}
