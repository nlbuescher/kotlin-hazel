package hazel.renderer

import hazel.math.FloatVector4

private var _api: RenderAPI.API = RenderAPI.API.OpenGL

interface RenderAPI {

    enum class API {
        None, OpenGL
    }

    fun init()

    fun setClearColor(color: FloatVector4)
    fun clear()

    fun drawIndexed(vertexArray: VertexArray)


    companion object {
        var api: API
            get() = _api
            set(new) = run { _api = new }
    }
}
