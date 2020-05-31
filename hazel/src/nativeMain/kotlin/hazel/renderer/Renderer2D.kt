package hazel.renderer

import hazel.core.Hazel
import hazel.core.profile
import hazel.math.*

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

	fun drawQuad(position: Vec2, size: Vec2, color: Vec4) {
		drawQuad(Vec3(position.x, position.y, 0f), size, color)
	}

	fun drawQuad(position: Vec3, size: Vec2, color: Vec4) {
		Hazel.profile("${this::class.qualifiedName}.drawQuad(${Vec3::class.qualifiedName},${Vec2::class.qualifiedName},${Vec4::class.qualifiedName}") {
			textureShader["u_Color"] = color
			textureShader["u_TilingFactor"] = 1f
			whiteTexture.bind()

			val transform = Mat4.IDENTITY.toMutableMat4().apply {
				translate(position)
				scale(Vec3(size.x, size.y, 1f))
			}
			textureShader["u_Transform"] = transform

			quadVertexArray.bind()
			RenderCommand.drawIndexed(quadVertexArray)
		}
	}

	fun drawQuad(position: Vec2, size: Vec2, texture: Texture2D, tilingFactor: Float = 1f, tintColor: Vec4 = Vec4.ONE) {
		drawQuad(Vec3(position.x, position.y, 0f), size, texture, tilingFactor, tintColor)
	}

	fun drawQuad(position: Vec3, size: Vec2, texture: Texture2D, tilingFactor: Float = 1f, tintColor: Vec4 = Vec4.ONE) {
		Hazel.profile("${this::class.qualifiedName}.drawQuad(${Vec3::class.qualifiedName},${Vec2::class.qualifiedName},${Texture2D::class.qualifiedName}") {
			textureShader["u_Color"] = tintColor
			textureShader["u_TilingFactor"] = tilingFactor
			texture.bind()

			val transform = Mat4.IDENTITY.toMutableMat4().apply {
				translate(position)
				scale(Vec3(size.x, size.y, 1f))
			}
			textureShader["u_Transform"] = transform

			quadVertexArray.bind()
			RenderCommand.drawIndexed(quadVertexArray)
		}
	}

	fun drawRotatedQuad(position: Vec2, size: Vec2, rotation: Float, color: Vec4) {
		drawRotatedQuad(Vec3(position.x, position.y, 0f), size, rotation, color)
	}

	fun drawRotatedQuad(position: Vec3, size: Vec2, rotation: Float, color: Vec4) {
		Hazel.profile("${this::class.qualifiedName}.drawRotatedQuad(${Vec3::class.qualifiedName},${Vec2::class.qualifiedName},${Vec4::class.qualifiedName}") {
			textureShader["u_Color"] = color
			textureShader["u_TilingFactor"] = 1f
			whiteTexture.bind()

			val transform = Mat4.IDENTITY.toMutableMat4().apply {
				translate(position)
				rotate(rotation, Vec3(0f, 0f, 1f))
				scale(Vec3(size.x, size.y, 1f))
			}
			textureShader["u_Transform"] = transform

			quadVertexArray.bind()
			RenderCommand.drawIndexed(quadVertexArray)
		}
	}

	fun drawRotatedQuad(position: Vec2, size: Vec2, rotation: Float, texture: Texture2D, tilingFactor: Float = 1f, tintColor: Vec4 = Vec4.ONE) {
		drawRotatedQuad(Vec3(position.x, position.y, 0f), size, rotation, texture, tilingFactor, tintColor)
	}

	fun drawRotatedQuad(position: Vec3, size: Vec2, rotation: Float, texture: Texture2D, tilingFactor: Float = 1f, tintColor: Vec4 = Vec4.ONE) {
		Hazel.profile("${this::class.qualifiedName}.drawRotatedQuad(${Vec3::class.qualifiedName},${Vec2::class.qualifiedName},${Texture2D::class.qualifiedName}") {
			textureShader["u_Color"] = tintColor
			textureShader["u_TilingFactor"] = tilingFactor
			texture.bind()

			val transform = Mat4.IDENTITY.toMutableMat4().apply {
				translate(position)
				rotate(rotation, Vec3(0f, 0f, 1f))
				scale(Vec3(size.x, size.y, 1f))
			}
			textureShader["u_Transform"] = transform

			quadVertexArray.bind()
			RenderCommand.drawIndexed(quadVertexArray)
		}
	}
}
