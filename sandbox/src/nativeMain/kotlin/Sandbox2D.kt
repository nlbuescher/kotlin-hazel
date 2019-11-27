import cimgui.igBegin
import cimgui.igColorEdit4
import cimgui.igEnd
import hazel.core.Event
import hazel.core.Layer
import hazel.core.TimeStep
import hazel.math.FloatMatrix4x4
import hazel.math.FloatVector2
import hazel.math.FloatVector3
import hazel.math.FloatVector4
import hazel.renderer.BufferElement
import hazel.renderer.BufferLayout
import hazel.renderer.OrthographicCameraController
import hazel.renderer.RenderCommand
import hazel.renderer.Renderer
import hazel.renderer.Renderer2D
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

    private val squareColor = FloatVector4(0f, 0f, 1f, 1f)

    override fun onAttach() {}

    override fun onDetach() {}

    override fun onUpdate(timeStep: TimeStep) {
        // update
        cameraController.onUpdate(timeStep)

        // render
        RenderCommand.setClearColor(FloatVector4(0.1f, 0.1f, 0.1f, 1f))
        RenderCommand.clear()

        Renderer2D.scene(cameraController.camera) {
            drawQuad(FloatVector2(-1f, 0f), FloatVector2(0.8f, 0.8f), FloatVector4(1f, 0f, 0f, 1f))
            drawQuad(FloatVector2(0.5f, -0.5f), FloatVector2(0.5f, 0.75f), FloatVector4(0f, 1f, 0f, 1f))
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
}
