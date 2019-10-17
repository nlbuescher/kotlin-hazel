package hazel.renderer

import hazel.math.Float4

private var _api: RenderAPI.API = RenderAPI.API.OpenGL

interface RenderAPI {

    enum class API {
        None, OpenGL
    }


    fun setClearColor(color: Float4)
    fun clear()

    fun drawIndexed(vertexArray: VertexArray)


    companion object {
        var api: API
            get() = _api
            set(new) = run { _api = new }
    }
}
