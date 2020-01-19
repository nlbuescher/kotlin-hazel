package hazel.renderer

import hazel.math.FloatMatrix4x4
import hazel.math.FloatVector2
import hazel.math.FloatVector3
import hazel.math.FloatVector4

private lateinit var quadVertexArray: VertexArray
private lateinit var textureShader: Shader
private lateinit var whiteTexture: Texture2D

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

        whiteTexture = Texture2D(1, 1)
        whiteTexture.setData(ubyteArrayOf(0xFFu, 0xFFu, 0xFFu, 0xFFu).asByteArray())

        textureShader = Shader("assets/shaders/Texture.glsl")
        textureShader.bind()
        textureShader["u_Texture"] = 0
    }

    fun shutdown() {
        quadVertexArray.dispose()
    }

    private fun beginScene(camera: OrthographicCamera) {
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
        textureShader["u_Color"] = color
        whiteTexture.bind()

        val transform = FloatMatrix4x4(1f).translate(position).scale(FloatVector3(size.x, size.y, 1f))
        textureShader["u_Transform"] = transform

        quadVertexArray.bind()
        RenderCommand.drawIndexed(quadVertexArray)
    }

    fun drawQuad(position: FloatVector2, size: FloatVector2, texture: Texture2D) =
        drawQuad(FloatVector3(position.x, position.y, 0f), size, texture)

    fun drawQuad(position: FloatVector3, size: FloatVector2, texture: Texture2D) {
        textureShader["u_Color"] = FloatVector4(1f)
        texture.bind()

        val transform = FloatMatrix4x4(1f).translate(position).scale(FloatVector3(size.x, size.y, 1f))
        textureShader["u_Transform"] = transform

        quadVertexArray.bind()
        RenderCommand.drawIndexed(quadVertexArray)
    }
}
