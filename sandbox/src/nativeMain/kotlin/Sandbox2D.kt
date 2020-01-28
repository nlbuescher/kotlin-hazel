import cimgui.internal.igText
import com.imgui.ImGui
import hazel.core.Event
import hazel.core.Hazel
import hazel.core.Layer
import hazel.core.TimeStep
import hazel.math.FloatVector2
import hazel.math.FloatVector3
import hazel.math.FloatVector4
import hazel.renderer.OrthographicCameraController
import hazel.renderer.RenderCommand
import hazel.renderer.Renderer2D
import hazel.renderer.Texture2D
import kotlin.system.measureNanoTime

class Sandbox2D : Layer("Sandbox2D") {
    private val profileResults = mutableListOf<ProfileResult>()

    private val cameraController = OrthographicCameraController(1280f / 720f)

    private val squareColor = FloatVector4(0f, 0f, 1f, 1f)

    private lateinit var checkerBoardTexture: Texture2D


    override fun onAttach() {
        checkerBoardTexture = Texture2D("assets/textures/Checkerboard.png")
    }

    override fun onDetach() {}

    override fun onUpdate(timeStep: TimeStep) {
        Hazel.debug { "start update" }
        profile("Sandbox2D.onUpdate") {
            // update
            profile("cameraController.onUpdate") {
                cameraController.onUpdate(timeStep)
            }

            // render
            profile("renderer prep") {
                RenderCommand.setClearColor(FloatVector4(0.1f, 0.1f, 0.1f, 1f))
                RenderCommand.clear()
            }

            profile("renderer draw") {
                Renderer2D.scene(cameraController.camera) {
                    drawQuad(FloatVector2(-1f, 0f), FloatVector2(0.8f, 0.8f), FloatVector4(1f, 0f, 0f, 1f))
                    drawQuad(FloatVector2(0.5f, -0.5f), FloatVector2(0.5f, 0.75f), FloatVector4(0f, 1f, 0f, 1f))
                    drawQuad(FloatVector3(0f, 0f, -0.1f), FloatVector2(10f, 10f), checkerBoardTexture)
                }
            }
        }
        Hazel.debug { "end update" }
    }

    override fun onImGuiRender() {
        with(ImGui) {
            begin("Settings")
            colorEdit4("Square Color", squareColor.asFloatArray())

            profileResults.forEach {
                igText("%.3fms  ${it.name}", it.time)
            }
            profileResults.clear()

            end()
        }
    }

    override fun onEvent(event: Event) {
        cameraController.onEvent(event)
    }


    private class ProfileResult(val name: String, val time: Float)

    private fun profile(name: String, block: () -> Unit) {
        val time: Float = measureNanoTime(block) / 1_000_000f
        profileResults.add(ProfileResult(name, time))
    }
}
