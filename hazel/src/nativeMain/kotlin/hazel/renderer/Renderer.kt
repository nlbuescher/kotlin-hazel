package hazel.renderer

object Renderer {
    var api: RenderAPI.API
        get() = RenderAPI.api
        set(new) = run { RenderAPI.api = new }

    fun beginScene() {

    }

    fun endScene() {

    }

    fun submit(vertexArray: VertexArray) {
        vertexArray.bind()
        RenderCommand.drawIndexed(vertexArray)
    }
}
