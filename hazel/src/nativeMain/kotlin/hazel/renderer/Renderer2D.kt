package hazel.renderer

import hazel.math.FloatMatrix4x4
import hazel.math.FloatVector2
import hazel.math.FloatVector3
import hazel.math.FloatVector4

private lateinit var quadVertexArray: VertexArray
private lateinit var flatColorShader: Shader
private lateinit var textureShader: Shader

object Renderer2D {

    fun init() {
        quadVertexArray = VertexArray()

        val squareVertexBuffer = vertexBufferOf(
            -0.5f, -0.5f, 0f, 0f, 0f,
            +0.5f, -0.5f, 0f, 1f, 0f,
            +0.5f, +0.5f, 0f, 1f, 1f,
            -0.5f, +0.5f, 0f, 0f, 1f
        )
        squareVertexBuffer.layout = BufferLayout(
            BufferElement(ShaderDataType.Float3, "a_Position"),
            BufferElement(ShaderDataType.Float2, "a_TextureCoordinate")
        )

        quadVertexArray.addVertexBuffer(squareVertexBuffer)
        quadVertexArray.indexBuffer = indexBufferOf(0u, 1u, 2u, 2u, 3u, 0u)

        flatColorShader = Shader("assets/shaders/FlatColor.glsl")
        textureShader = Shader("assets/shaders/Texture.glsl")

        textureShader.bind()
        textureShader["u_Texture"] = 0
    }

    fun shutdown() {
        quadVertexArray.dispose()
        flatColorShader.dispose()
    }

    private fun beginScene(camera: OrthographicCamera) {
        flatColorShader.bind()
        flatColorShader["u_ViewProjection"] = camera.viewProjectionMatrix

        textureShader.bind()
        textureShader["u_ViewProjection"] = camera.viewProjectionMatrix
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

    fun drawQuad(position: FloatVector2, size: FloatVector2, texture: Texture2D) =
        drawQuad(FloatVector3(position.x, position.y, 0f), size, texture)

    fun drawQuad(position: FloatVector3, size: FloatVector2, texture: Texture2D) {
        textureShader.bind()

        val transform = FloatMatrix4x4(1f).translate(position).scale(FloatVector3(size.x, size.y, 1f))
        textureShader["u_Transform"] = transform

        texture.bind()

        quadVertexArray.bind()
        RenderCommand.drawIndexed(quadVertexArray)
    }
}
