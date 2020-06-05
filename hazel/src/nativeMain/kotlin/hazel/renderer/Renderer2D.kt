package hazel.renderer

import hazel.core.Disposable
import hazel.core.Hazel
import hazel.core.profile
import hazel.math.*
import kotlinx.cinterop.*
import kotlinx.cinterop.internal.CStruct

// Use manually defined CStruct types and CPointers to enable manual memory management for performance

@CStruct("struct { float p0; float p1; }")
class Float2(rawPtr: NativePtr) : CStructVar(rawPtr) {
	companion object : CStructVar.Type(8, 4)

	var x: Float
		get() = memberAt<FloatVar>(0).value
		set(value) {
			memberAt<FloatVar>(0).value = value
		}

	var y: Float
		get() = memberAt<FloatVar>(4).value
		set(value) {
			memberAt<FloatVar>(4).value = value
		}
}

@CStruct("struct { float p0; float p1; float p2; }")
class Float3(rawPtr: NativePtr) : CStructVar(rawPtr) {
	companion object : CStructVar.Type(12, 4)

	var x: Float
		get() = memberAt<FloatVar>(0).value
		set(value) {
			memberAt<FloatVar>(0).value = value
		}

	var y: Float
		get() = memberAt<FloatVar>(4).value
		set(value) {
			memberAt<FloatVar>(4).value = value
		}

	var z: Float
		get() = memberAt<FloatVar>(8).value
		set(value) {
			memberAt<FloatVar>(8).value = value
		}
}

@CStruct("struct { float p0; float p1; float p2; float p3; }")
class Float4(rawPtr: NativePtr) : CStructVar(rawPtr) {
	companion object : CStructVar.Type(16, 4)

	var x: Float
		get() = memberAt<FloatVar>(0).value
		set(value) {
			memberAt<FloatVar>(0).value = value
		}

	var y: Float
		get() = memberAt<FloatVar>(4).value
		set(value) {
			memberAt<FloatVar>(4).value = value
		}

	var z: Float
		get() = memberAt<FloatVar>(8).value
		set(value) {
			memberAt<FloatVar>(8).value = value
		}

	var w: Float
		get() = memberAt<FloatVar>(12).value
		set(value) {
			memberAt<FloatVar>(12).value = value
		}
}

@CStruct("struct { struct { float p0; float p1; float p2; } p0; struct { float p0; float p1; float p2; float p3; } p1; struct { float p0; float p1; } p2; }")
class QuadVertex(rawPtr: NativePtr) : CStructVar(rawPtr) {
	companion object : CStructVar.Type(36, 4)

	val position: Float3
		get() = memberAt(0)

	val color: Float4
		get() = memberAt(12)

	val textureCoordinate: Float2
		get() = memberAt(28)
}

private class Renderer2DData : Disposable {
	val maxQuads: Int = 10_000
	val maxVertices: Int = maxQuads * 4
	val maxIndices: Int = maxQuads * 6

	lateinit var quadVertexArray: VertexArray
	lateinit var quadVertexBuffer: VertexBuffer
	lateinit var textureShader: Shader
	lateinit var whiteTexture: Texture2D

	var quadIndexCount: Int = 0
	lateinit var quadVertexData: Pinned<ByteArray>
	lateinit var quadVertices: CArrayPointer<QuadVertex>
	var currentVertex: Int = 0

	override fun dispose() {
		if (::quadVertexArray.isInitialized) quadVertexArray.dispose()
		if (::textureShader.isInitialized) textureShader.dispose()
		if (::whiteTexture.isInitialized) whiteTexture.dispose()
	}
}

private val data = Renderer2DData()

object Renderer2D {
	fun init() {
		Hazel.profile("Renderer2D.init()") {
			with(data) {
				quadVertexArray = VertexArray()

				quadVertexBuffer = VertexBuffer(data.maxVertices * sizeOf<QuadVertex>().toInt())
				quadVertexBuffer.layout = BufferLayout(
					BufferElement(ShaderDataType.Float3, "a_Position"),
					BufferElement(ShaderDataType.Float4, "a_Color"),
					BufferElement(ShaderDataType.Float2, "a_TextureCoordinate")
				)
				quadVertexArray.addVertexBuffer(quadVertexBuffer)

				// create primitive array for passing to kotlin APIs
				quadVertexData = ByteArray(maxVertices * sizeOf<QuadVertex>().toInt()).pin()
				// reinterpret pointer to byte array as pointer to QuadVertex array
				quadVertices = quadVertexData.addressOf(0).reinterpret()

				val quadIndices = UIntArray(maxIndices)
				var i = 0
				var offset = 0u
				while (i < maxIndices) {
					quadIndices[i + 0] = offset + 0u
					quadIndices[i + 1] = offset + 1u
					quadIndices[i + 2] = offset + 2u

					quadIndices[i + 3] = offset + 2u
					quadIndices[i + 4] = offset + 3u
					quadIndices[i + 5] = offset + 0u

					i += 6
					offset += 4u
				}
				quadVertexArray.indexBuffer = IndexBuffer(quadIndices)

				whiteTexture = Texture2D(1, 1)
				whiteTexture.setData(ubyteArrayOf(0xFFu, 0xFFu, 0xFFu, 0xFFu).asByteArray())

				textureShader = Shader("assets/shaders/texture.glsl")
				textureShader.bind()
				textureShader["u_Texture"] = 0
			}
		}
	}

	fun shutdown() {
		Hazel.profile("Renderer2D.shutdown()") {
			data.quadVertexData.unpin()
			data.dispose()
		}
	}

	private fun beginScene(camera: OrthographicCamera) {
		Hazel.profile("Renderer2D.beginScene(OrthographicCamera)") {
			with(data) {
				textureShader.bind()
				textureShader["u_ViewProjection"] = camera.viewProjectionMatrix

				quadIndexCount = 0
				currentVertex = 0
			}
		}
	}

	private fun endScene() {
		Hazel.profile("Renderer2D.endScene()") {
			with (data) {
				val byteCount = currentVertex * sizeOf<QuadVertex>().toInt()
				quadVertexBuffer.setData(quadVertexData.get(), byteCount)
				flush()
			}
		}
	}

	fun scene(camera: OrthographicCamera, block: Renderer2D.() -> Unit) {
		beginScene(camera)
		this.block()
		endScene()
	}

	fun flush() {
		Hazel.profile("Renderer2D.flush()") {
			RenderCommand.drawIndexed(data.quadVertexArray, data.quadIndexCount)
		}
	}

	fun drawQuad(position: Vec2, size: Vec2, color: Vec4) {
		drawQuad(Vec3(position.x, position.y, 0f), size, color)
	}

	fun drawQuad(position: Vec3, size: Vec2, color: Vec4) {
		Hazel.profile("Renderer2D.drawQuad(Vec3, Vec2, Vec4)") {
			with(data) {
				quadVertices[currentVertex].also { vertex ->
					vertex.position.apply { x = position.x; y = position.y; z = position.z }
					vertex.color.apply { x = color.x; y = color.y; z = color.z; w = color.w }
					vertex.textureCoordinate.apply { x = 0f; y = 0f }
				}
				currentVertex += 1
				quadVertices[currentVertex].also { vertex ->
					vertex.position.apply { x = position.x + size.x; y = position.y; z = position.z }
					vertex.color.apply { x = color.x; y = color.y; z = color.z; w = color.w }
					vertex.textureCoordinate.apply { x = 1f; y = 0f }
				}
				currentVertex += 1
				quadVertices[currentVertex].also { vertex ->
					vertex.position.apply { x = position.x + size.x; y = position.y + size.y; z = position.z }
					vertex.color.apply { x = color.x; y = color.y; z = color.z; w = color.w }
					vertex.textureCoordinate.apply { x = 1f; y = 1f }
				}
				currentVertex += 1
				quadVertices[currentVertex].also { vertex ->
					vertex.position.apply { x = position.x; y = position.y + size.y; z = position.z }
					vertex.color.apply { x = color.x; y = color.y; z = color.z; w = color.w }
					vertex.textureCoordinate.apply { x = 0f; y = 1f }
				}
				currentVertex += 1
				quadIndexCount += 6

				/*
				textureShader["u_Color"] = color
				textureShader["u_TilingFactor"] = 1f
				whiteTexture.bind()

				textureShader["u_Transform"] = Mat4.IDENTITY.toMutableMat4().apply {
					translate(position)
					scale(Vec3(size.x, size.y, 1f))
				}

				quadVertexArray.bind()
				RenderCommand.drawIndexed(quadVertexArray)
				*/
			}
		}
	}

	fun drawQuad(position: Vec2, size: Vec2, texture: Texture2D, tilingFactor: Float = 1f, tintColor: Vec4 = Vec4.ONE) {
		drawQuad(Vec3(position.x, position.y, 0f), size, texture, tilingFactor, tintColor)
	}

	fun drawQuad(position: Vec3, size: Vec2, texture: Texture2D, tilingFactor: Float = 1f, tintColor: Vec4 = Vec4.ONE) {
		Hazel.profile("Renderer2D.drawQuad(Vec3, Vec2, Texture2D, Float, Vec4)") {
			with(data) {
				textureShader["u_Color"] = tintColor
				textureShader["u_TilingFactor"] = tilingFactor
				texture.bind()

				textureShader["u_Transform"] = Mat4.IDENTITY.toMutableMat4().apply {
					translate(position)
					scale(Vec3(size.x, size.y, 1f))
				}

				quadVertexArray.bind()
				RenderCommand.drawIndexed(quadVertexArray)
			}
		}
	}

	fun drawRotatedQuad(position: Vec2, size: Vec2, rotation: Float, color: Vec4) {
		drawRotatedQuad(Vec3(position.x, position.y, 0f), size, rotation, color)
	}

	fun drawRotatedQuad(position: Vec3, size: Vec2, rotation: Float, color: Vec4) {
		Hazel.profile("Renderer2D.drawRotatedQuad(Vec3, Vec2, Float, Vec4)") {
			with(data) {
				textureShader["u_Color"] = color
				textureShader["u_TilingFactor"] = 1f
				whiteTexture.bind()

				textureShader["u_Transform"] = Mat4.IDENTITY.toMutableMat4().apply {
					translate(position)
					rotate(rotation, Vec3.FORWARD)
					scale(Vec3(size.x, size.y, 1f))
				}

				quadVertexArray.bind()
				RenderCommand.drawIndexed(quadVertexArray)
			}
		}
	}

	fun drawRotatedQuad(position: Vec2, size: Vec2, rotation: Float, texture: Texture2D, tilingFactor: Float = 1f, tintColor: Vec4 = Vec4.ONE) {
		drawRotatedQuad(Vec3(position.x, position.y, 0f), size, rotation, texture, tilingFactor, tintColor)
	}

	fun drawRotatedQuad(position: Vec3, size: Vec2, rotation: Float, texture: Texture2D, tilingFactor: Float = 1f, tintColor: Vec4 = Vec4.ONE) {
		Hazel.profile("Renderer2D.drawRotatedQuad(Vec3, Vec2, Float, Texture2D, Float, Vec4)") {
			with(data) {
				textureShader["u_Color"] = tintColor
				textureShader["u_TilingFactor"] = tilingFactor
				texture.bind()

				textureShader["u_Transform"] = Mat4.IDENTITY.toMutableMat4().apply {
					translate(position)
					rotate(rotation, Vec3.FORWARD)
					scale(Vec3(size.x, size.y, 1f))
				}

				quadVertexArray.bind()
				RenderCommand.drawIndexed(quadVertexArray)
			}
		}
	}
}
