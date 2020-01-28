package hazel.core

import com.imgui.ImGui
import com.imgui.impl.ImGuiGLFW
import com.imgui.impl.ImguiOpenGL3
import kotlinx.cinterop.pointed

open class ImGuiLayer : Overlay("ImGuiLayer") {

    lateinit var glfw: ImGuiGLFW
    lateinit var openGL3: ImguiOpenGL3

    override fun onAttach() {
        with(ImGui) {
            createContext()
            val io = getIO()
            //io.ConfigFlags = io.ConfigFlags or ImGuiConfigFlags_NavEnableKeyboard.convert() // enable keyboard controls
            //io.ConfigFlags = io.ConfigFlags or ImGuiConfigFlags_NavEnableGamepad.convert() // enable gamepad controls
            //io.ConfigFlags = io.ConfigFlags or ImGuiConfigFlags_DockingEnable.convert() // enable docking
            //io.ConfigFlags = io.ConfigFlags or ImGuiConfigFlags_ViewportsEnable.convert() // enable multi-viewport / platform windows
            //io.ConfigFlags = io.ConfigFlags or ImGuiConfigFlags_ViewportsNoTaskBarIcon.convert()
            //io.ConfigFlags = io.ConfigFlags or ImGuiConfigFlags_ViewportsNoMerge.convert()

            // Setup Dear ImGui Style
            styleColorsDark()
            // igStyleColorsClassic(null)

            // when viewports are enabled we tweak WindowRounding/WindowBg so platform windows can look odentical to regular ones
            //val style = igGetStyle()!!.pointed
            //if (io.ConfigFlags and ImGuiConfigFlags_ViewportsEnable.convert() != 0) {
            //    style.WindowRounding = 0f
            //    style.Colors[ImGuiCol_WindowBg.value.convert()].w = 1f
            //}

            // Setup Platform / Renderer Bindings
            glfw = ImGuiGLFW(Hazel.application.window.internal, true)
            openGL3 = ImguiOpenGL3("#version 410")
        }
    }

    override fun onDetach() {
        openGL3.close()
        glfw.close()
        ImGui.destroyContext()
    }

    fun begin() {
        openGL3.newFrame()
        glfw.newFrame()
        ImGui.newFrame()
    }

    fun end() {
        val io = ImGui.getIO().ptr.pointed
        val (windowX, windowY) = Hazel.application.window.size
        io.DisplaySize.apply { x = windowX.toFloat(); y = windowY.toFloat() }

        // Rendering
        ImGui.render()
        openGL3.renderDrawData(ImGui.getDrawData())

//        if (io.ConfigFlags and ImGuiConfigFlags_ViewportsEnable.convert() != 0) {
//            val backupCurrentContext = glfwGetCurrentContext()
//            igUpdatePlatformWindows()
//            igRenderPlatformWindowsDefault(null, null)
//            glfwMakeContextCurrent(backupCurrentContext)
//        }
    }
}
