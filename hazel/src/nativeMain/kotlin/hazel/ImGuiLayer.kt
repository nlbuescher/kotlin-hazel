package hazel

import cimgui.ImGuiBackendFlags_HasMouseCursors
import cimgui.ImGuiBackendFlags_HasSetMousePos
import cimgui.ImGuiIO_AddInputCharacter
import cimgui.ImGuiKey_.*
import cimgui.igCreateContext
import cimgui.igGetDrawData
import cimgui.igGetIO
import cimgui.igImplOpenGL3Init
import cimgui.igImplOpenGL3NewFrame
import cimgui.igImplOpenGL3RenderDrawData
import cimgui.igNewFrame
import cimgui.igRender
import cimgui.igShowDemoWindow
import cimgui.igStyleColorsDark
import copengl.glViewport
import kotlinx.cinterop.Arena
import kotlinx.cinterop.BooleanVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.get
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import kotlinx.cinterop.set
import kotlinx.cinterop.value

class ImGuiLayer : Overlay("ImGuiLayer") {

    private val scope = Arena()

    private var lastTime: Float = 0f

    override fun onAttach() {
        igCreateContext(null)
        igStyleColorsDark(null)

        val io = igGetIO()!!.pointed

        with(io) {
            BackendFlags = BackendFlags or ImGuiBackendFlags_HasMouseCursors.toInt()
            BackendFlags = BackendFlags or ImGuiBackendFlags_HasSetMousePos.toInt()

            KeyMap[ImGuiKey_Tab.ordinal] = Key.TAB.value
            KeyMap[ImGuiKey_LeftArrow.ordinal] = Key.LEFT.value
            KeyMap[ImGuiKey_RightArrow.ordinal] = Key.RIGHT.value
            KeyMap[ImGuiKey_UpArrow.ordinal] = Key.UP.value
            KeyMap[ImGuiKey_DownArrow.ordinal] = Key.DOWN.value
            KeyMap[ImGuiKey_PageUp.ordinal] = Key.PAGE_UP.value
            KeyMap[ImGuiKey_PageDown.ordinal] = Key.PAGE_DOWN.value
            KeyMap[ImGuiKey_Home.ordinal] = Key.HOME.value
            KeyMap[ImGuiKey_End.ordinal] = Key.END.value
            KeyMap[ImGuiKey_Insert.ordinal] = Key.INSERT.value
            KeyMap[ImGuiKey_Delete.ordinal] = Key.DELETE.value
            KeyMap[ImGuiKey_Backspace.ordinal] = Key.BACKSPACE.value
            KeyMap[ImGuiKey_Space.ordinal] = Key.SPACE.value
            KeyMap[ImGuiKey_Enter.ordinal] = Key.ENTER.value
            KeyMap[ImGuiKey_Escape.ordinal] = Key.ESCAPE.value
            KeyMap[ImGuiKey_A.ordinal] = Key.A.value
            KeyMap[ImGuiKey_C.ordinal] = Key.C.value
            KeyMap[ImGuiKey_V.ordinal] = Key.V.value
            KeyMap[ImGuiKey_X.ordinal] = Key.X.value
            KeyMap[ImGuiKey_Y.ordinal] = Key.Y.value
            KeyMap[ImGuiKey_Z.ordinal] = Key.Z.value
        }

        igImplOpenGL3Init("#version 410")
    }

    private val show: BooleanVar = scope.alloc()

    override fun onUpdate() {
        val io = igGetIO()!!.pointed
        val window = Hazel.application.window
        io.DisplaySize.x = window.size.first.toFloat()
        io.DisplaySize.y = window.size.second.toFloat()

        val time = Hazel.time.toFloat()
        io.DeltaTime = if (lastTime > 0f) time - lastTime else 1f / 60f
        lastTime = time

        igImplOpenGL3NewFrame()
        igNewFrame()

        igShowDemoWindow(show.ptr)

        igRender()
        igImplOpenGL3RenderDrawData(igGetDrawData())
    }


    override fun onEvent(event: Event) {
        with(Event.Dispatcher(event)) {
            dispatch(::onMouseButtonPressed)
            dispatch(::onMouseButtonReleased)
            dispatch(::onMouseMoved)
            dispatch(::onMouseScrolled)
            dispatch(::onKeyPressed)
            dispatch(::onKeyReleased)
            dispatch(::onKeyTyped)
            dispatch(::onWindowResize)
        }
    }


    private fun onMouseButtonPressed(event: MouseButtonPressedEvent): Boolean {
        val io = igGetIO()!!.pointed
        io.MouseDown[event.button.value].value = true

        return false
    }

    private fun onMouseButtonReleased(event: MouseButtonReleasedEvent): Boolean {
        val io = igGetIO()!!.pointed
        io.MouseDown[event.button.value].value = false

        return false
    }

    private fun onMouseMoved(event: MouseMovedEvent): Boolean {
        val io = igGetIO()!!.pointed
        io.MousePos.x = event.x
        io.MousePos.y = event.y

        return false
    }

    private fun onMouseScrolled(event: MouseScrolledEvent): Boolean {
        val io = igGetIO()!!.pointed
        io.MouseWheelH += event.xOffset
        io.MouseWheel += event.yOffset

        return false
    }

    private fun onKeyPressed(event: KeyPressedEvent): Boolean {
        val io = igGetIO()!!.pointed
        io.KeysDown[event.key.value].value = true

        io.KeyCtrl = io.KeysDown[Key.LEFT_CONTROL.value].value || io.KeysDown[Key.RIGHT_CONTROL.value].value
        io.KeyShift = io.KeysDown[Key.LEFT_SHIFT.value].value || io.KeysDown[Key.RIGHT_SHIFT.value].value
        io.KeyAlt = io.KeysDown[Key.LEFT_ALT.value].value || io.KeysDown[Key.RIGHT_ALT.value].value
        io.KeySuper = io.KeysDown[Key.LEFT_SUPER.value].value || io.KeysDown[Key.RIGHT_SUPER.value].value

        return false
    }

    private fun onKeyReleased(event: KeyReleasedEvent): Boolean {
        val io = igGetIO()!!.pointed
        io.KeysDown[event.key.value].value = false

        io.KeyCtrl = io.KeysDown[Key.LEFT_CONTROL.value].value || io.KeysDown[Key.RIGHT_CONTROL.value].value
        io.KeyShift = io.KeysDown[Key.LEFT_SHIFT.value].value || io.KeysDown[Key.RIGHT_SHIFT.value].value
        io.KeyAlt = io.KeysDown[Key.LEFT_ALT.value].value || io.KeysDown[Key.RIGHT_ALT.value].value
        io.KeySuper = io.KeysDown[Key.LEFT_SUPER.value].value || io.KeysDown[Key.RIGHT_SUPER.value].value

        return false
    }

    private fun onKeyTyped(event: KeyTypedEvent): Boolean {
        val io = igGetIO()
        if (event.key.value in 1..0xFFFF) {
            ImGuiIO_AddInputCharacter(io, event.key.value.convert())
        }

        return false
    }

    private fun onWindowResize(event: WindowResizeEvent): Boolean {
        val io = igGetIO()!!.pointed
        io.DisplaySize.x = event.width.toFloat()
        io.DisplaySize.y = event.height.toFloat()
        io.DisplayFramebufferScale.x = 1f
        io.DisplayFramebufferScale.y = 1f

        glViewport(0, 0, event.width, event.height)

        return false
    }
}
