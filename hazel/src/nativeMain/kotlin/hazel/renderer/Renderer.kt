package hazel.renderer

enum class RenderAPI {
    None, OpenGL
}

private var _renderAPI: RenderAPI = RenderAPI.OpenGL

class Renderer {
    companion object {
        var renderAPI
            get() = _renderAPI
            set(value) = run { _renderAPI = value }
    }
}
