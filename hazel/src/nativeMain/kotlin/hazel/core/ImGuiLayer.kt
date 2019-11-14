package hazel.core

import cglfw.glfwGetCurrentContext
import cglfw.glfwMakeContextCurrent
import cimgui.ImGuiCol_.ImGuiCol_WindowBg
import cimgui.ImGuiConfigFlags_DockingEnable
import cimgui.ImGuiConfigFlags_NavEnableKeyboard
import cimgui.ImGuiConfigFlags_ViewportsEnable
import cimgui.igCreateContext
import cimgui.igDestroyContext
import cimgui.igGetDrawData
import cimgui.igGetIO
import cimgui.igGetStyle
import cimgui.igImplGlfwInitForOpenGL
import cimgui.igImplGlfwNewFrame
import cimgui.igImplGlfwShutdown
import cimgui.igImplOpenGL3Init
import cimgui.igImplOpenGL3NewFrame
import cimgui.igImplOpenGL3RenderDrawData
import cimgui.igImplOpenGL3Shutdown
import cimgui.igNewFrame
import cimgui.igRender
import cimgui.igRenderPlatformWindowsDefault
import cimgui.igShowDemoWindow
import cimgui.igStyleColorsDark
import cimgui.igUpdatePlatformWindows
import kotlinx.cinterop.BooleanVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.get
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr

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

    override fun onImGuiRender() = memScoped {
        val show = alloc<BooleanVar>()
        igShowDemoWindow(show.ptr)
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
