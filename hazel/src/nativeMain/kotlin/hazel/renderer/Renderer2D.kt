package hazel.renderer

import hazel.core.Hazel
import hazel.core.profile
import hazel.math.FloatMatrix4x4
import hazel.math.FloatVector2
import hazel.math.FloatVector3
import hazel.math.FloatVector4

private lateinit var quadVertexArray: VertexArray
private lateinit var textureShader: Shader
private lateinit var whiteTexture: Texture2D

object Renderer2D {

    fun init() {
        Hazel.profile(::init) {
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
    }

    fun shutdown() {
        Hazel.profile(::shutdown) {
            quadVertexArray.dispose()
        }
    }

    private fun beginScene(camera: OrthographicCamera) {
        Hazel.profile(::beginScene) {
            textureShader.bind()
            textureShader["u_ViewProjection"] = camera.viewProjectionMatrix
        }
    }

    private fun endScene() {
        Hazel.profile(::endScene) {
        }
    }

    fun scene(camera: OrthographicCamera, block: Renderer2D.() -> Unit) {
        beginScene(camera)
        this.block()
        endScene()
    }

    fun drawQuad(position: FloatVector2, size: FloatVector2, color: FloatVector4) {
        drawQuad(FloatVector3(position.x, position.y, 0f), size, color)
    }

    fun drawQuad(position: FloatVector3, size: FloatVector2, color: FloatVector4) {
        Hazel.profile("${this::class.qualifiedName}.drawQuad(${FloatVector3::class.qualifiedName},${FloatVector2::class.qualifiedName},${FloatVector4::class.qualifiedName}") {
            textureShader["u_Color"] = color
            textureShader["u_TilingFactor"] = 1f
            whiteTexture.bind()

            val transform = FloatMatrix4x4(1f)
                .translate(position)
                .scale(FloatVector3(size.x, size.y, 1f))
            textureShader["u_Transform"] = transform

            quadVertexArray.bind()
            RenderCommand.drawIndexed(quadVertexArray)
        }
    }

    fun drawQuad(position: FloatVector2, size: FloatVector2, texture: Texture2D, tilingFactor: Float = 1f, tintColor: FloatVector4 = FloatVector4(1f)) {
        drawQuad(FloatVector3(position.x, position.y, 0f), size, texture, tilingFactor, tintColor)
    }

    fun drawQuad(position: FloatVector3, size: FloatVector2, texture: Texture2D, tilingFactor: Float = 1f, tintColor: FloatVector4 = FloatVector4(1f)) {
        Hazel.profile("${this::class.qualifiedName}.drawQuad(${FloatVector3::class.qualifiedName},${FloatVector2::class.qualifiedName},${Texture2D::class.qualifiedName}") {
            textureShader["u_Color"] = tintColor
            textureShader["u_TilingFactor"] = tilingFactor
            texture.bind()

            val transform = FloatMatrix4x4(1f)
                .translate(position)
                .scale(FloatVector3(size.x, size.y, 1f))
            textureShader["u_Transform"] = transform

            quadVertexArray.bind()
            RenderCommand.drawIndexed(quadVertexArray)
        }
    }

    fun drawRotatedQuad(position: FloatVector2, size: FloatVector2, rotation: Float, color: FloatVector4) {
        drawRotatedQuad(FloatVector3(position.x, position.y, 0f), size, rotation, color)
    }

    fun drawRotatedQuad(position: FloatVector3, size: FloatVector2, rotation: Float, color: FloatVector4) {
        Hazel.profile("${this::class.qualifiedName}.drawRotatedQuad(${FloatVector3::class.qualifiedName},${FloatVector2::class.qualifiedName},${FloatVector4::class.qualifiedName}") {
            textureShader["u_Color"] = color
            textureShader["u_TilingFactor"] = 1f
            whiteTexture.bind()

            val transform = FloatMatrix4x4(1f)
                .translate(position)
                .rotate(rotation, FloatVector3(0f, 0f, 1f))
                .scale(FloatVector3(size.x, size.y, 1f))
            textureShader["u_Transform"] = transform

            quadVertexArray.bind()
            RenderCommand.drawIndexed(quadVertexArray)
        }
    }

    fun drawRotatedQuad(position: FloatVector2, size: FloatVector2, rotation: Float, texture: Texture2D, tilingFactor: Float = 1f, tintColor: FloatVector4 = FloatVector4(1f)) {
        drawRotatedQuad(FloatVector3(position.x, position.y, 0f), size, rotation, texture, tilingFactor, tintColor)
    }

    fun drawRotatedQuad(position: FloatVector3, size: FloatVector2, rotation: Float, texture: Texture2D, tilingFactor: Float = 1f, tintColor: FloatVector4 = FloatVector4(1f)) {
        Hazel.profile("${this::class.qualifiedName}.drawRotatedQuad(${FloatVector3::class.qualifiedName},${FloatVector2::class.qualifiedName},${Texture2D::class.qualifiedName}") {
            textureShader["u_Color"] = tintColor
            textureShader["u_TilingFactor"] = tilingFactor
            texture.bind()

            val transform = FloatMatrix4x4(1f)
                .translate(position)
                .rotate(rotation, FloatVector3(0f, 0f, 1f))
                .scale(FloatVector3(size.x, size.y, 1f))
            textureShader["u_Transform"] = transform

            quadVertexArray.bind()
            RenderCommand.drawIndexed(quadVertexArray)
        }
    }
}
