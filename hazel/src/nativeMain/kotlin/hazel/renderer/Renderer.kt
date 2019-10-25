package hazel.renderer

import hazel.math.FloatMatrix4x4
import hazel.renderer.opengl.OpenGLShader

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

    fun submit(shader: Shader, vertexArray: VertexArray, transform: FloatMatrix4x4 = FloatMatrix4x4()) {
        shader as OpenGLShader

        shader.bind()
        shader.uploadUniform("u_ViewProjection", sceneData.viewProjectionMatrix)
        shader.uploadUniform("u_Transform", transform)

        vertexArray.bind()
        RenderCommand.drawIndexed(vertexArray)
    }
}
