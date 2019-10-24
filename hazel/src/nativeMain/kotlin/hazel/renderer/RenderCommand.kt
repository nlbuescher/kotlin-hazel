package hazel.renderer

import hazel.math.Vector4
import hazel.renderer.opengl.OpenGLRenderAPI

private var renderAPI: RenderAPI = OpenGLRenderAPI()

object RenderCommand {
    var api: RenderAPI.API
        get() = RenderAPI.api
        set(new) = run { RenderAPI.api = new }


    fun setClearColor(color: Vector4<Float>) {
        renderAPI.setClearColor(color)
    }

    fun clear() {
        renderAPI.clear()
    }

    fun drawIndexed(vertexArray: VertexArray) {
        renderAPI.drawIndexed(vertexArray)
    }
}