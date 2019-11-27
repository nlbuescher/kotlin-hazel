package hazel.renderer

import hazel.math.FloatMatrix4x4

class SceneData {
    var viewProjectionMatrix = FloatMatrix4x4()
}

private val sceneData = SceneData()

object Renderer {
    var api: RenderAPI.API
        get() = RenderAPI.api
        set(new) = run { RenderAPI.api = new }

    fun init() {
        RenderCommand.init()
        Renderer2D.init()
    }

    fun onWindowResize(width: Int, height: Int) {
        RenderCommand.setViewport(0, 0, width, height)
    }

    private fun beginScene(camera: OrthographicCamera) {
        sceneData.viewProjectionMatrix = camera.viewProjectionMatrix
    }

    private fun endScene() {

    }

    fun scene(camera: OrthographicCamera, block: Renderer.() -> Unit) {
        beginScene(camera)
        this.block()
        endScene()
    }

    fun submit(shader: Shader, vertexArray: VertexArray, transform: FloatMatrix4x4 = FloatMatrix4x4()) {
        shader.bind()
        shader["u_ViewProjection"] = sceneData.viewProjectionMatrix
        shader["u_Transform"] = transform

        vertexArray.bind()
        RenderCommand.drawIndexed(vertexArray)
    }
}
