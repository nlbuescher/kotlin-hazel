package hazel.imgui

import cimgui.internal.*
import com.imgui.ImGui
import com.imgui.impl.ImGuiGLFW
import com.imgui.impl.ImguiOpenGL3
import hazel.core.Hazel
import hazel.core.Overlay
import hazel.core.application
import hazel.core.profile
import kotlinx.cinterop.convert
import kotlinx.cinterop.pointed
import kotlinx.cinterop.sizeOf

open class ImGuiLayer : Overlay("ImGuiLayer") {
	lateinit var glfw: ImGuiGLFW
	lateinit var openGL3: ImguiOpenGL3

	override fun onAttach() {
		Hazel.profile("ImGuiLayer.onAttach()") {
			// setup Dear ImGui context
			ImGui.debugCheckVersionAndDataLayout(
				versionStr = ImGui.getVersion()!!,
				szIo = sizeOf<ImGuiIO>().convert(),
				szStyle = sizeOf<ImGuiStyle>().convert(),
				szVec2 = sizeOf<ImVec2>().convert(),
				szVec4 = sizeOf<ImVec4>().convert(),
				szDrawvert = sizeOf<ImDrawVert>().convert(),
				szDrawidx = sizeOf<ImDrawIdxVar>().convert()
			)
			ImGui.createContext()
			val io = ImGui.getIO().ptr.pointed
			io.ConfigFlags = io.ConfigFlags or ImGuiConfigFlags_NavEnableKeyboard.convert() // enable keyboard controls
			//io.ConfigFlags = io.ConfigFlags or ImGuiConfigFlags_NavEnableGamepad.convert() // enable gamepad controls
			//io.ConfigFlags = io.ConfigFlags or ImGuiConfigFlags_DockingEnable.convert() // enable docking
			//io.ConfigFlags = io.ConfigFlags or ImGuiConfigFlags_ViewportsEnable.convert() // enable multi-viewport / platform windows
			//io.ConfigFlags = io.ConfigFlags or ImGuiConfigFlags_ViewportsNoTaskBarIcon.convert()
			//io.ConfigFlags = io.ConfigFlags or ImGuiConfigFlags_ViewportsNoMerge.convert()

			// setup Dear ImGui style
			ImGui.styleColorsDark()
			//ImGui.styleColorsClassic()

			// when viewports are enabled we tweak WindowRounding/WindowBg so platform windows can look identical to regular ones
			//val style = ImGui.getStyle().ptr.pointed
			//if (io.ConfigFlags and ImGuiConfigFlags_ViewportsEnable.convert() != 0) {
			//    style.WindowRounding = 0f
			//    style.Colors[ImGuiCol_WindowBg.convert()].w = 1f
			//}

			// setup Platform / Renderer Bindings
			glfw = ImGuiGLFW(Hazel.application.window.nativeWindow, true)
			openGL3 = ImguiOpenGL3("#version 330")
		}
	}

	override fun onDetach() {
		Hazel.profile("ImGuiLayer.onDetach()") {
			openGL3.close()
			glfw.close()
			ImGui.destroyContext()
		}
	}

	fun begin() {
		Hazel.profile("ImGuiLayer.begin()") {
			openGL3.newFrame()
			glfw.newFrame()
			ImGui.newFrame()
		}
	}

	fun end() {
		Hazel.profile("ImGuiLayer.end()") {
			val io = ImGui.getIO().ptr.pointed
			val (width, height) = Hazel.application.window.size
			io.DisplaySize.x = width.toFloat()
			io.DisplaySize.y = height.toFloat()

			// Rendering
			ImGui.render()
			openGL3.renderDrawData(ImGui.getDrawData())

			//if (io.ConfigFlags and ImGuiConfigFlags_ViewportsEnable.convert() != 0) {
			//	val backupCurrentContext = glfwGetCurrentContext()
			//	igUpdatePlatformWindows()
			//	igRenderPlatformWindowsDefault(null, null)
			//	glfwMakeContextCurrent(backupCurrentContext)
			//}
		}
	}
}
