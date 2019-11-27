package hazel.renderer

import hazel.math.FloatMatrix4x4
import hazel.math.FloatVector2
import hazel.math.FloatVector3
import hazel.math.FloatVector4
import hazel.renderer.opengl.OpenGLShader


private lateinit var quadVertexArray: VertexArray
private lateinit var flatColorShader: Shader

object Renderer2D {

    fun init() {
        quadVertexArray = VertexArray()

        val squareVertexBuffer = vertexBufferOf(
            -0.5f, -0.5f, 0f,
            +0.5f, -0.5f, 0f,
            +0.5f, +0.5f, 0f,
            -0.5f, +0.5f, 0f
        )
        squareVertexBuffer.layout = BufferLayout(
            BufferElement(ShaderDataType.Float3, "a_Position")
        )

        quadVertexArray.addVertexBuffer(squareVertexBuffer)
        quadVertexArray.indexBuffer = indexBufferOf(0u, 1u, 2u, 2u, 3u, 0u)

        flatColorShader = Shader("assets/shaders/FlatColor.glsl")
    }

    fun shutdown() {
        quadVertexArray.dispose()
        flatColorShader.dispose()
    }

    private fun beginScene(camera: OrthographicCamera) {
        flatColorShader.bind()
        flatColorShader["u_ViewProjection"] = camera.viewProjectionMatrix
    }

    private fun endScene() {}

    fun scene(camera: OrthographicCamera, block: Renderer2D.() -> Unit) {
        beginScene(camera)
        this.block()
        endScene()
    }

    fun drawQuad(position: FloatVector2, size: FloatVector2, color: FloatVector4) =
        drawQuad(FloatVector3(position.x, position.y, 0f), size, color)

    fun drawQuad(position: FloatVector3, size: FloatVector2, color: FloatVector4) {
        flatColorShader.bind()
        flatColorShader["u_Color"] = color

        val transform = FloatMatrix4x4(1f).translate(position).scale(FloatVector3(size.x, size.y, 1f))
        flatColorShader["u_Transform"] = transform

        quadVertexArray.bind()
        RenderCommand.drawIndexed(quadVertexArray)
    }
}
