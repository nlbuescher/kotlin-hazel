package hazel.renderer.opengl

import copengl.GL_COLOR_BUFFER_BIT
import copengl.GL_DEPTH_BUFFER_BIT
import copengl.GL_TRIANGLES
import copengl.GL_UNSIGNED_INT
import copengl.glClearColor
import copengl.glDrawElements
import hazel.math.FloatVector4
import hazel.renderer.RenderAPI
import hazel.renderer.VertexArray
import opengl.glClear

class OpenGLRenderAPI : RenderAPI {
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
