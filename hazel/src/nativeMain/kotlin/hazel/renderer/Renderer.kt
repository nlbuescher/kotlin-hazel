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

    fun submit(shader: Shader, vertexArray: VertexArray) {
        shader.bind()
        shader.uploadUniform("u_ViewProjection", sceneData.viewProjectionMatrix)

        vertexArray.bind()
        RenderCommand.drawIndexed(vertexArray)
    }
}
