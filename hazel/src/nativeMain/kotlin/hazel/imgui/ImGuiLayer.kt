package hazel.imgui

import cimgui.internal.*
import cimgui.internal.ImDrawVert
import cimgui.internal.ImGuiIO
import cimgui.internal.ImGuiStyle
import cimgui.internal.ImVec2
import cimgui.internal.ImVec4
import com.imgui.*
import com.imgui.ImGuiCol
import com.imgui.ImGuiConfigFlags
import com.imgui.impl.*
import com.kgl.glfw.*
import hazel.core.*
import hazel.events.*
import kotlinx.cinterop.*

open class ImGuiLayer : Overlay("ImGuiLayer") {
	private lateinit var glfw: ImGuiGlfw
	private lateinit var openGL3: ImGuiOpenGL3

	override fun onAttach() {
		Hazel.profile("ImGuiLayer.onAttach()") {
			// setup Dear ImGui context
			ImGui.debugCheckVersionAndDataLayout(
				versionStr = ImGui.getVersion()!!,
				szIo = sizeOf<ImGuiIO>().toULong(),
				szStyle = sizeOf<ImGuiStyle>().toULong(),
				szVec2 = sizeOf<ImVec2>().toULong(),
				szVec4 = sizeOf<ImVec4>().toULong(),
				szDrawvert = sizeOf<ImDrawVert>().toULong(),
				szDrawidx = sizeOf<ImDrawIdxVar>().toULong()
			)
			ImGui.createContext()
			val io = ImGui.getIO()
			io.configFlags = io.configFlags or ImGuiConfigFlags.NavEnableKeyboard // enable keyboard controls
			//io.configFlags = io.configFlags or ImGuiConfigFlags.NavEnableGamepad // enable gamepad controls
			io.configFlags = io.configFlags or ImGuiConfigFlags.DockingEnable // enable docking
			io.configFlags = io.configFlags or ImGuiConfigFlags.ViewportsEnable // enable multi-viewport / platform windows
			//io.configFlags = io.configFlags or ImGuiConfigFlags.ViewportsNoTaskBarIcon
			//io.configFlags = io.configFlags or ImGuiConfigFlags.ViewportsNoMerge

			// setup Dear ImGui style
			ImGui.styleColorsDark()
			//ImGui.styleColorsClassic()

			// when viewports are enabled we tweak WindowRounding/WindowBg so platform windows can look identical to regular ones
			val style = ImGui.getStyle().ptr.pointed
			if (ImGuiConfigFlags.ViewportsEnable in io.configFlags) {
			    style.WindowRounding = 0f
			    style.Colors[ImGuiCol.WindowBg.value].w = 1f
			}

			// setup Platform / Renderer Bindings
			glfw = ImGuiGlfw(Hazel.application.window.nativeWindow, true)
			openGL3 = ImGuiOpenGL3("#version 330")
		}
	}

	override fun onDetach() {
		Hazel.profile("ImGuiLayer.onDetach()") {
			openGL3.close()
			glfw.close()
			ImGui.destroyContext()
		}
	}

	override fun onEvent(event: Event) {
		val io = ImGui.getIO()
		event.isHandled = event.isHandled || (event is MouseEvent && io.wantCaptureMouse)
		event.isHandled = event.isHandled || (event is KeyEvent && io.wantCaptureKeyboard)
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
			val io = ImGui.getIO()
			val (width, height) = Hazel.application.window.size
			io.displaySize = Vec2(width.toFloat(), height.toFloat())

			// Rendering
			ImGui.render()
			openGL3.renderDrawData(ImGui.getDrawData())

			if (ImGuiConfigFlags.ViewportsEnable in io.configFlags) {
				val backupCurrentContext = Glfw.currentContext
				ImGui.updatePlatformWindows()
				ImGui.renderPlatformWindowsDefault()
				Glfw.currentContext = backupCurrentContext
			}
		}
	}
}
