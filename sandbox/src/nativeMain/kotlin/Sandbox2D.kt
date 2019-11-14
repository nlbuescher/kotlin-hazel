import cimgui.igBegin
import cimgui.igColorEdit4
import cimgui.igEnd
import hazel.core.Event
import hazel.core.Layer
import hazel.core.TimeStep
import hazel.math.FloatMatrix4x4
import hazel.math.FloatVector3
import hazel.math.FloatVector4
import hazel.renderer.BufferElement
import hazel.renderer.BufferLayout
import hazel.renderer.OrthographicCameraController
import hazel.renderer.RenderCommand
import hazel.renderer.Renderer
import hazel.renderer.Shader
import hazel.renderer.ShaderDataType
import hazel.renderer.VertexArray
import hazel.renderer.indexBufferOf
import hazel.renderer.opengl.OpenGLShader
import hazel.renderer.vertexBufferOf
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned

class Sandbox2D : Layer("Sandbox2D") {

    private val cameraController = OrthographicCameraController(1280f / 720f)

    // temp

    private lateinit var squareVertexArray: VertexArray
    private lateinit var flatColorShader: Shader

    private val squareColor = FloatVector4(0f, 0f, 1f, 1f)


    override fun onAttach() {
        squareVertexArray = VertexArray()

        val squareVertexBuffer = vertexBufferOf(
            -0.5f, -0.5f, 0f,
            +0.5f, -0.5f, 0f,
            +0.5f, +0.5f, 0f,
            -0.5f, +0.5f, 0f
        )
        squareVertexBuffer.layout = BufferLayout(
            BufferElement(ShaderDataType.Float3, "a_Position")
        )

        squareVertexArray.addVertexBuffer(squareVertexBuffer)
        squareVertexArray.indexBuffer = indexBufferOf(0u, 1u, 2u, 2u, 3u, 0u)

        flatColorShader = Shader("assets/shaders/FlatColor.glsl")
    }

    override fun onDetach() {

    }

    override fun onUpdate(timeStep: TimeStep) {
        // update
        cameraController.onUpdate(timeStep)

        // render
        RenderCommand.setClearColor(FloatVector4(0.1f, 0.1f, 0.1f, 1f))
        RenderCommand.clear()

        Renderer.scene(cameraController.camera) {
            flatColorShader.bind()
            (flatColorShader as OpenGLShader).uploadUniform("u_Color", squareColor)

            submit(flatColorShader, squareVertexArray, FloatMatrix4x4(1f).scale(FloatVector3(1.5f)))
        }
    }

    override fun onImGuiRender() {
        igBegin("Settings", null, 0)
        squareColor.asFloatArray().usePinned {
            igColorEdit4("Square Color", it.addressOf(0), 0)
        }
        igEnd()
    }

    override fun onEvent(event: Event) {
        cameraController.onEvent(event)
    }

    override fun dispose() {
        flatColorShader.dispose()
        squareVertexArray.dispose()
    }
}
