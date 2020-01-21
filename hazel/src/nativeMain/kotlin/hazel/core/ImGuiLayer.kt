package hazel.core

import cglfw.glfwGetCurrentContext
import cglfw.glfwMakeContextCurrent
import imgui.ImGuiCol_.ImGuiCol_WindowBg
import imgui.ImGuiConfigFlags_DockingEnable
import imgui.ImGuiConfigFlags_NavEnableKeyboard
import imgui.ImGuiConfigFlags_ViewportsEnable
import imgui.igCreateContext
import imgui.igDestroyContext
import imgui.igGetDrawData
import imgui.igGetIO
import imgui.igGetStyle
import imgui.igImplGlfwInitForOpenGL
import imgui.igImplGlfwNewFrame
import imgui.igImplGlfwShutdown
import imgui.igImplOpenGL3Init
import imgui.igImplOpenGL3NewFrame
import imgui.igImplOpenGL3RenderDrawData
import imgui.igImplOpenGL3Shutdown
import imgui.igNewFrame
import imgui.igRender
import imgui.igRenderPlatformWindowsDefault
import imgui.igStyleColorsDark
import imgui.igUpdatePlatformWindows
import kotlinx.cinterop.convert
import kotlinx.cinterop.get
import kotlinx.cinterop.pointed

open class ImGuiLayer : Overlay("ImGuiLayer") {
    override fun onAttach() {
        igCreateContext(null)
        val io = igGetIO()!!.pointed
        io.ConfigFlags = io.ConfigFlags or ImGuiConfigFlags_NavEnableKeyboard.convert() // enable keyboard controls
        //io.ConfigFlags = io.ConfigFlags or ImGuiConfigFlags_NavEnableGamepad.convert() // enable gamepad controls
        io.ConfigFlags = io.ConfigFlags or ImGuiConfigFlags_DockingEnable.convert() // enable docking
        io.ConfigFlags = io.ConfigFlags or ImGuiConfigFlags_ViewportsEnable.convert() // enable multi-viewport / platform windows
        //io.ConfigFlags = io.ConfigFlags or ImGuiConfigFlags_ViewportsNoTaskBarIcon.convert()
        //io.ConfigFlags = io.ConfigFlags or ImGuiConfigFlags_ViewportsNoMerge.convert()

        // Setup Dear ImGui Style
        igStyleColorsDark(null)
        // igStyleColorsClassic(null)

        // when viewports are enabled we tweak WindowRounding/WindowBg so platform windows can look odentical to regular ones
        val style = igGetStyle()!!.pointed
        if (io.ConfigFlags and ImGuiConfigFlags_ViewportsEnable.convert() != 0) {
            style.WindowRounding = 0f
            style.Colors[ImGuiCol_WindowBg.value.convert()].w = 1f
        }

        val window = Hazel.application.window.ptr

        // Setup Platform / Renderer Bindings
        igImplGlfwInitForOpenGL(window, true)
        igImplOpenGL3Init("#version 410")
    }

    override fun onDetach() {
        igImplOpenGL3Shutdown()
        igImplGlfwShutdown()
        igDestroyContext(null)
    }

    fun begin() {
        igImplOpenGL3NewFrame()
        igImplGlfwNewFrame()
        igNewFrame()
    }

    fun end() {
        val io = igGetIO()!!.pointed
        val (windowX, windowY) = Hazel.application.window.size
        io.DisplaySize.apply { x = windowX.toFloat(); y = windowY.toFloat() }

        // Rendering
        igRender()
        igImplOpenGL3RenderDrawData(igGetDrawData())

        if (io.ConfigFlags and ImGuiConfigFlags_ViewportsEnable.convert() != 0) {
            val backupCurrentContext = glfwGetCurrentContext()
            igUpdatePlatformWindows()
            igRenderPlatformWindowsDefault(null, null)
            glfwMakeContextCurrent(backupCurrentContext)
        }
    }
}
