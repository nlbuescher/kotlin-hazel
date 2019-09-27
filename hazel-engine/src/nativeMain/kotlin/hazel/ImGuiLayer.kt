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
import com.kgl.opengl.glViewport
import kotlinx.cinterop.BooleanVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.get
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import kotlinx.cinterop.set
import kotlinx.cinterop.value

class ImGuiLayer : Overlay("ImGuiLayer") {
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

    private val show: BooleanVar = nativeHeap.alloc()

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


//private object OpenGL3 {
//    private val scope = Arena()
//
//    private var glslVersionString: String = ""
//
//    private var fontTexture = scope.alloc<GLuintVar> { value = 0u }
//
//    private var shaderHandle: GLuint = 0u
//    private var vertHandle: GLuint = 0u
//    private var fragHandle: GLuint = 0u
//
//    private var attribLocationTex: Int = 0
//    private var attribLocationProjMtx: Int = 0
//
//    private var attribLocationPosition: Int = 0
//    private var attribLocationUV: Int = 0
//    private var attribLocationColor: Int = 0
//
//    private var vboHandle = scope.alloc<UIntVar> { value = 0u }
//    private var elementsHandle = scope.alloc<UIntVar> { value = 0u }
//
//
//    // functions
//
//    fun init(glslVersion: String? = null): Boolean {
//        val io = igGetIO()!!.pointed
//        io.BackendFlags = io.BackendFlags or ImGuiBackendFlags_RendererHasViewports.convert()
//        io.BackendRendererName = "imgui_impl_opengl3".cstr.placeTo(scope)
//
//        // Store GLSL version string so we can refer to it later in case we recreate shaders. Note: GLSL version is NOT the same as GL version. Leave this to NULL if unsure.
//        glslVersionString = glslVersion ?: "#version 130"
//
//        if (io.ConfigFlags and ImGuiConfigFlags_ViewportsEnable.convert() != 0)
//            initPlatformInterface()
//
//        return true
//    }
//
//    fun shutdown() {
//        shutdownPlatformInterface()
//        destroyDeviceObjects()
//        scope.clear()
//    }
//
//    fun newFrame() {
//        if (fontTexture.value == 0u)
//            createDeviceObjects()
//    }
//
//    fun renderDrawData(drawData: CPointer<ImDrawData>): Unit = memScoped {
//        val io = igGetIO()!!.pointed
//        // Avoid rendering when minimized, scale coordinates for retina displays (screen coordinates != framebuffer coordinates)
//        val fbWidth = (drawData.pointed.DisplaySize.x * io.DisplayFramebufferScale.x).toInt()
//        val fbHeight = (drawData.pointed.DisplaySize.y * io.DisplayFramebufferScale.y).toInt()
//        if (fbWidth <= 0 || fbHeight <= 0)
//            return
//        ImDrawData_ScaleClipRects(drawData, cValue { x = io.DisplayFramebufferScale.x; y = io.DisplayFramebufferScale.y })
//
//        // Backup GL state
//        val lastActiveTexture = alloc<GLenumVar>(); glGetIntegerv(GL_ACTIVE_TEXTURE, lastActiveTexture.ptr.reinterpret())
//        glActiveTexture(GL_TEXTURE0)
//        val lastProgram = alloc<GLintVar>(); glGetIntegerv(GL_CURRENT_PROGRAM, lastProgram.ptr)
//        val lastTexture = alloc<GLintVar>(); glGetIntegerv(GL_TEXTURE_BINDING_2D, lastTexture.ptr)
////#ifdef GL_SAMPLER_BINDING
//        val lastSampler = alloc<GLintVar>(); glGetIntegerv(GL_SAMPLER_BINDING, lastSampler.ptr)
////#endif
//        val lastArrayBuffer = alloc<GLintVar>(); glGetIntegerv(GL_ARRAY_BUFFER_BINDING, lastArrayBuffer.ptr)
//        val lastVertexArray = alloc<GLintVar>(); glGetIntegerv(GL_VERTEX_ARRAY_BINDING, lastVertexArray.ptr)
////#ifdef GL_POLYGON_MODE
//        val lastPolygonMode = allocArray<GLintVar>(2); glGetIntegerv(GL_POLYGON_MODE, lastPolygonMode)
////#endif
//        val lastViewport = allocArray<GLintVar>(4); glGetIntegerv(GL_VIEWPORT, lastViewport)
//        val lastScissorBox = allocArray<GLintVar>(4); glGetIntegerv(GL_SCISSOR_BOX, lastScissorBox)
//        val lastBlendSrcRgb = alloc<GLenumVar>(); glGetIntegerv(GL_BLEND_SRC_RGB, lastBlendSrcRgb.ptr.reinterpret())
//        val lastBlendDstRgb = alloc<GLenumVar>(); glGetIntegerv(GL_BLEND_DST_RGB, lastBlendDstRgb.ptr.reinterpret())
//        val lastBlendSrcAlpha = alloc<GLenumVar>(); glGetIntegerv(GL_BLEND_SRC_ALPHA, lastBlendSrcAlpha.ptr.reinterpret())
//        val lastBlendDstAlpha = alloc<GLenumVar>(); glGetIntegerv(GL_BLEND_DST_ALPHA, lastBlendDstAlpha.ptr.reinterpret())
//        val lastBlendEquationRgb = alloc<GLenumVar>(); glGetIntegerv(GL_BLEND_EQUATION_RGB, lastBlendEquationRgb.ptr.reinterpret())
//        val lastBlendEquationAlpha = alloc<GLenumVar>(); glGetIntegerv(GL_BLEND_EQUATION_ALPHA, lastBlendEquationAlpha.ptr.reinterpret())
//        val lastEnableBlend = glIsEnabled(GL_BLEND)
//        val lastEnableCullFace = glIsEnabled(GL_CULL_FACE)
//        val lastEnableDepthTest = glIsEnabled(GL_DEPTH_TEST)
//        val lastEnableScissorTest = glIsEnabled(GL_SCISSOR_TEST)
//        var clipOriginLowerLeft = true
////#ifdef GL_CLIP_ORIGIN
//        val lastClipOrigin = alloc<GLenumVar> { value = 0u }; glGetIntegerv(GL_CLIP_ORIGIN, lastClipOrigin.ptr.reinterpret()) // Support for GL 4.5's glClipControl(GL_UPPER_LEFT)
//        if (lastClipOrigin.value == GL_UPPER_LEFT)
//            clipOriginLowerLeft = false
////#endif
//
//        // Setup render state: alpha-blending enabled, no face culling, no depth testing, scissor enabled, polygon fill
//        glEnable(GL_BLEND)
//        glBlendEquation(GL_FUNC_ADD)
//        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
//        glDisable(GL_CULL_FACE)
//        glDisable(GL_DEPTH_TEST)
//        glEnable(GL_SCISSOR_TEST)
////#ifdef GL_POLYGON_MODE
//        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)
////#endif
//
//
//        // Setup viewport, orthographic projection matrix
//        // Our visible imgui space lies from drawData->DisplayPos (top left) to drawData->DisplayPos+data_data->DisplaySize (bottom right). DisplayMin is typically (0,0) for single viewport apps.
//        glViewport(0, 0, fbWidth, fbHeight)
//        val l = drawData.pointed.DisplayPos.x
//        val r = drawData.pointed.DisplayPos.x + drawData.pointed.DisplaySize.x
//        val t = drawData.pointed.DisplayPos.y
//        val b = drawData.pointed.DisplayPos.y + drawData.pointed.DisplaySize.y
//        val orthoProjection = allocArray<FloatVar>(16)
//        orthoProjection[0] = 2f / (r - l)
//        orthoProjection[1] = 0f
//        orthoProjection[2] = 0f
//        orthoProjection[3] = 0f
//
//        orthoProjection[4] = 0f
//        orthoProjection[5] = 2f / (t - b)
//        orthoProjection[6] = 0f
//        orthoProjection[7] = 0f
//
//        orthoProjection[8] = 0f
//        orthoProjection[9] = 0f
//        orthoProjection[10] = -1f
//        orthoProjection[11] = 0f
//
//        orthoProjection[12] = (r + l) / (l - r)
//        orthoProjection[13] = (t + b) / (b - t)
//        orthoProjection[14] = 0f
//        orthoProjection[15] = 1f
//
//        glUseProgram(shaderHandle)
//        glUniform1i(attribLocationTex, 0)
//        glUniformMatrix4fv(attribLocationProjMtx, 1, GL_FALSE.convert(), orthoProjection)
////#ifdef GL_SAMPLER_BINDING
//        glBindSampler(0u, 0u) // We use combined texture/sampler state. Applications using GL 3.3 may set that otherwise.
////#endif
//
//        // Recreate the VAO every time
//        // (This is to easily allow multiple GL contexts. VAO are not shared among GL contexts, and we don't track creation/deletion of windows so we don't have an obvious key to use to cache them.)
//        val vaoHandle = alloc<GLuintVar>()
//        glGenVertexArrays(1, vaoHandle.ptr)
//        glBindVertexArray(vaoHandle.value)
//        glBindBuffer(GL_ARRAY_BUFFER, vboHandle.value)
//        glEnableVertexAttribArray(attribLocationPosition.convert())
//        glEnableVertexAttribArray(attribLocationUV.convert())
//        glEnableVertexAttribArray(attribLocationColor.convert())
//        glVertexAttribPointer(attribLocationPosition.convert(), 2, GL_FLOAT, GL_FALSE.convert(), ImDrawVert.size.convert(), ImDrawVert_pos_offset())
//        glVertexAttribPointer(attribLocationUV.convert(), 2, GL_FLOAT, GL_FALSE.convert(), ImDrawVert.size.convert(), ImDrawVert_uv_offset())
//        glVertexAttribPointer(attribLocationColor.convert(), 4, GL_UNSIGNED_BYTE, GL_TRUE.convert(), ImDrawVert.size.convert(), ImDrawVert_col_offset())
//
//        // Draw
//        val pos = drawData.pointed.DisplayPos
//        for (n in 0 until drawData.pointed.CmdListsCount) {
//            val cmdList = drawData.pointed.CmdLists!![n]
//            val idxBufferOffset = alloc<CPointerVar<ImDrawIdxVar>> { value = 0L.toCPointer() }
//
//            glBindBuffer(GL_ARRAY_BUFFER, vboHandle.value)
//            glBufferData(GL_ARRAY_BUFFER, cmdList!!.pointed.VtxBuffer.Size * ImDrawVert.size, cmdList.pointed.VtxBuffer.Data, GL_STREAM_DRAW)
//
//            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementsHandle.value)
//            glBufferData(GL_ELEMENT_ARRAY_BUFFER, cmdList.pointed.IdxBuffer.Size * ImDrawIdxVar.size, cmdList.pointed.IdxBuffer.Data, GL_STREAM_DRAW)
//
//            for (cmd_i in 0 until cmdList.pointed.CmdBuffer.Size) {
//                val pcmd = cmdList.pointed.CmdBuffer.Data!![cmd_i].ptr
//                if (pcmd.pointed.UserCallback != null) {
//                    // User callback (registered via ImDrawList::AddCallback)
//                    pcmd.pointed.UserCallback!!(cmdList, pcmd)
//
//                } else {
//                    // Project scissor/clipping rectangles into framebuffer space
//                    val clipRect = alloc<ImVec4> {
//                        x = (pcmd.pointed.ClipRect.x - pos.x)
//                        y = (pcmd.pointed.ClipRect.y - pos.y)
//                        z = (pcmd.pointed.ClipRect.z - pos.x)
//                        w = (pcmd.pointed.ClipRect.w - pos.y)
//                    }
//
//                    if (clipRect.x < fbWidth && clipRect.y < fbHeight && clipRect.z >= 0.0f && clipRect.w >= 0.0f) {
//                        // Apply scissor/clipping rectangle
//                        if (clipOriginLowerLeft)
//                            glScissor(clipRect.x.toInt(), (fbHeight - clipRect.w).toInt(), (clipRect.z - clipRect.x).toInt(), (clipRect.w - clipRect.y).toInt())
//                        else
//                            glScissor(clipRect.x.toInt(), clipRect.y.toInt(), clipRect.z.toInt(), clipRect.w.toInt()) // Support for GL 4.5's glClipControl(GL_UPPER_LEFT)
//
//                        // Bind texture, Draw
//                        glBindTexture(GL_TEXTURE_2D, pcmd.pointed.TextureId.toLong().toUInt())
//                        glDrawElements(GL_TRIANGLES, pcmd.pointed.ElemCount.toInt(), GL_UNSIGNED_SHORT, idxBufferOffset.value.toLong().toCPointer<CPointed>())
//                    }
//                }
//                idxBufferOffset.value += pcmd.pointed.ElemCount.toInt()
//            }
//        }
//        glDeleteVertexArrays(1, vaoHandle.ptr)
//
//        // Restore modified GL state
//        glUseProgram(lastProgram.value.convert())
//        glBindTexture(GL_TEXTURE_2D, lastTexture.value.convert())
////#ifdef GL_SAMPLER_BINDING
//        glBindSampler(0u, lastSampler.value.convert())
////#endif
//        glActiveTexture(lastActiveTexture.value.convert())
//        glBindBuffer(GL_ARRAY_BUFFER, lastArrayBuffer.value.convert())
//        glBlendEquationSeparate(lastBlendEquationRgb.value.convert(), lastBlendEquationAlpha.value.convert())
//        glBlendFuncSeparate(lastBlendSrcRgb.value.convert(), lastBlendDstRgb.value.convert(), lastBlendSrcAlpha.value.convert(), lastBlendDstAlpha.value.convert())
//        if (lastEnableBlend != GL_TRUE.convert()) glEnable(GL_BLEND) else glDisable(GL_BLEND)
//        if (lastEnableCullFace != GL_TRUE.convert()) glEnable(GL_CULL_FACE) else glDisable(GL_CULL_FACE)
//        if (lastEnableDepthTest != GL_TRUE.convert()) glEnable(GL_DEPTH_TEST) else glDisable(GL_DEPTH_TEST)
//        if (lastEnableScissorTest != GL_TRUE.convert()) glEnable(GL_SCISSOR_TEST) else glDisable(GL_SCISSOR_TEST)
////#ifdef GL_POLYGON_MODE
//        glPolygonMode(GL_FRONT_AND_BACK, lastPolygonMode[0].convert())
////#endif
//        glViewport(lastViewport[0], lastViewport[1], lastViewport[2], lastViewport[3])
//        glScissor(lastScissorBox[0], lastScissorBox[1], lastScissorBox[2], lastScissorBox[3])
//    }
//
//    fun createFontsTexture(): Boolean = memScoped {
//        // Build texture atlas
//        val io = igGetIO()!!.pointed
//        val pixels = alloc<CPointerVar<UByteVar>>()
//        val width = alloc<IntVar>()
//        val height = alloc<IntVar>()
//        ImFontAtlas_GetTexDataAsRGBA32(io.Fonts, pixels.ptr, width.ptr, height.ptr, null)   // Load as RGBA 32-bits (75% of the memory is wasted, but default font is so small) because it is more likely to be compatible with user's existing shaders. If your ImTextureId represent a higher-level concept than just a GL texture id, consider calling GetTexDataAsAlpha8() instead to save on GPU memory.
//
//        // Upload texture to graphics system
//        val lastTexture = alloc<GLintVar>()
//        glGetIntegerv(GL_TEXTURE_BINDING_2D, lastTexture.ptr)
//        glGenTextures(1, fontTexture.ptr)
//        glBindTexture(GL_TEXTURE_2D, fontTexture.value)
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR.convert())
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR.convert())
//        if (GL_UNPACK_ROW_LENGTH != 0u) {
//            glPixelStorei(GL_UNPACK_ROW_LENGTH, 0)
//        }
//        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA.toInt(), width.value, height.value, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels.value)
//
//        // Store our identifier
//        io.Fonts!!.pointed.TexID = fontTexture.value.toLong().toCPointer()
//
//        // Restore state
//        glBindTexture(GL_TEXTURE_2D, lastTexture.value.convert())
//
//        return@memScoped true
//    }
//
//    fun destroyFontsTexture() {
//        if (fontTexture.value != 0u) {
//            val io = igGetIO()!!.pointed
//            glDeleteTextures(1, fontTexture.ptr)
//            io.Fonts!!.pointed.TexID = 0L.toCPointer()
//            fontTexture.value = 0u
//        }
//    }
//
//    private fun checkShader(handle: GLuint, desc: String): Boolean = memScoped {
//        val status = alloc<IntVar>()
//        val logLength = alloc<IntVar>()
//        glGetShaderiv(handle, GL_COMPILE_STATUS, status.ptr)
//        glGetShaderiv(handle, GL_INFO_LOG_LENGTH, logLength.ptr)
//        if (status.value == GL_FALSE.toInt()) {
//            Hazel.coreError("ERROR: igOpenGL3CreateDeviceObjects: failed to compile $desc!")
//        }
//        if (logLength.value > 0) {
//            Hazel.coreError(glGetShaderInfoLog(handle))
//        }
//        return@memScoped status.value == GL_TRUE.toInt()
//    }
//
//    private fun checkProgram(handle: GLuint, desc: String): Boolean = memScoped {
//        val status = alloc<IntVar>()
//        val logLength = alloc<IntVar>()
//        glGetProgramiv(handle, GL_LINK_STATUS, status.ptr)
//        glGetProgramiv(handle, GL_INFO_LOG_LENGTH, logLength.ptr)
//        if (status.value == GL_FALSE.toInt()) {
//            Hazel.coreError("ERROR: igOpenGL3CreateDeviceObjects: failed to link $desc!")
//        }
//        if (logLength.value > 0) {
//            Hazel.coreError(glGetProgramInfoLog(handle))
//        }
//        return@memScoped status.value == GL_TRUE.toInt()
//    }
//
//    fun createDeviceObjects(): Boolean = memScoped {
//        // Backup GL state
//        val lastTexture = alloc<GLintVar>()
//        val lastArrayBuffer = alloc<GLintVar>()
//        val lastVertexArray = alloc<GLintVar>()
//        glGetIntegerv(GL_TEXTURE_BINDING_2D, lastTexture.ptr)
//        glGetIntegerv(GL_ARRAY_BUFFER_BINDING, lastArrayBuffer.ptr)
//        glGetIntegerv(GL_VERTEX_ARRAY_BINDING, lastVertexArray.ptr)
//
//        // Parse GLSL version string
//        val glslVersion = alloc<IntVar> { value = 130 }
//        sscanf(glslVersionString, "#version %d", glslVersion.ptr)
//
//        val vertexShaderGlsl120 = """
//            uniform mat4 ProjMtx;
//            attribute vec2 Position;
//            attribute vec2 UV;
//            attribute vec4 Color;
//            varying vec2 Frag_UV;
//            varying vec4 Frag_Color;
//            void main()
//            {
//                Frag_UV = UV;
//                Frag_Color = Color;
//                gl_Position = ProjMtx * vec4(Position.xy,0,1);
//            }
//            """.trimIndent()
//
//        val vertexShaderGlsl130 = """
//            uniform mat4 ProjMtx;
//            in vec2 Position;
//            in vec2 UV;
//            in vec4 Color;
//            out vec2 Frag_UV;
//            out vec4 Frag_Color;
//            void main()
//            {
//                Frag_UV = UV;
//                Frag_Color = Color;
//                gl_Position = ProjMtx * vec4(Position.xy,0,1);
//            }
//            """.trimIndent()
//
//        val vertexShaderGlsl300Es = """
//            precision mediump float;
//            layout (location = 0) in vec2 Position;
//            layout (location = 1) in vec2 UV;
//            layout (location = 2) in vec4 Color;
//            uniform mat4 ProjMtx;
//            out vec2 Frag_UV;
//            out vec4 Frag_Color;
//            void main()
//            {
//                Frag_UV = UV;
//                Frag_Color = Color;
//                gl_Position = ProjMtx * vec4(Position.xy,0,1);
//            }
//            """.trimIndent()
//
//        val vertexShaderGlsl410Core = """
//            layout (location = 0) in vec2 Position;
//            layout (location = 1) in vec2 UV;
//            layout (location = 2) in vec4 Color;
//            uniform mat4 ProjMtx;
//            out vec2 Frag_UV;
//            out vec4 Frag_Color;
//            void main()
//            {
//            Frag_UV = UV;
//            Frag_Color = Color;
//            gl_Position = ProjMtx * vec4(Position.xy,0,1);
//            }
//            """.trimIndent()
//
//        val fragmentShaderGlsl120 = """
//            ifdef GL_ES
//                precision mediump float;
//            #endif
//            uniform sampler2D Texture;
//            varying vec2 Frag_UV;
//            varying vec4 Frag_Color;
//            void main()
//            {
//                gl_FragColor = Frag_Color * texture2D(Texture, Frag_UV.st);
//            }
//            """.trimIndent()
//
//        val fragmentShaderGlsl130 = """
//            uniform sampler2D Texture;
//            in vec2 Frag_UV;
//            in vec4 Frag_Color;
//            out vec4 Out_Color;
//            void main()
//            {
//                Out_Color = Frag_Color * texture(Texture, Frag_UV.st);
//            }
//            """.trimIndent()
//
//        val fragmentShaderGlsl300Es = """
//            precision mediump float;
//            uniform sampler2D Texture;
//            in vec2 Frag_UV;
//            in vec4 Frag_Color;
//            layout (location = 0) out vec4 Out_Color;
//            void main()
//            {
//                Out_Color = Frag_Color * texture(Texture, Frag_UV.st);
//            }
//            """.trimIndent()
//
//        val fragmentShaderGlsl410Core = """
//            in vec2 Frag_UV;
//            in vec4 Frag_Color;
//            uniform sampler2D Texture;
//            layout (location = 0) out vec4 Out_Color;
//            void main()
//            {
//                Out_Color = Frag_Color * texture(Texture, Frag_UV.st);
//            }
//            """.trimIndent()
//
//        // Select shaders matching our GLSL versions
//        val (vertexShader, fragmentShader) = when {
//            glslVersion.value < 130 -> vertexShaderGlsl120 to fragmentShaderGlsl120
//            glslVersion.value >= 410 -> vertexShaderGlsl410Core to fragmentShaderGlsl410Core
//            glslVersion.value == 300 -> vertexShaderGlsl300Es to fragmentShaderGlsl300Es
//            else -> vertexShaderGlsl130 to fragmentShaderGlsl130
//        }
//
//        // Create shaders
//        val vertexShaderWithVersion = "$glslVersionString\n$vertexShader"
//        vertHandle = glCreateShader(GL_VERTEX_SHADER)
//        glShaderSource(vertHandle, vertexShaderWithVersion)
//        glCompileShader(vertHandle)
//        checkShader(vertHandle, "vertex shader")
//
//        val fragmentShaderWithVersion = arrayOf("$glslVersionString\n", fragmentShader).toCStringArray(scope)
//        fragHandle = glCreateShader(GL_FRAGMENT_SHADER)
//        glShaderSource(fragHandle, 2, fragmentShaderWithVersion, null)
//        glCompileShader(fragHandle)
//        checkShader(fragHandle, "fragment shader")
//
//        shaderHandle = glCreateProgram()
//        glAttachShader(shaderHandle, vertHandle)
//        glAttachShader(shaderHandle, fragHandle)
//        glLinkProgram(shaderHandle)
//        checkProgram(shaderHandle, "shader program")
//
//        attribLocationTex = glGetUniformLocation(shaderHandle, "Texture".cstr)
//        attribLocationProjMtx = glGetUniformLocation(shaderHandle, "ProjMtx".cstr)
//        attribLocationPosition = glGetAttribLocation(shaderHandle, "Position".cstr)
//        attribLocationUV = glGetAttribLocation(shaderHandle, "UV".cstr)
//        attribLocationColor = glGetAttribLocation(shaderHandle, "Color".cstr)
//
//        // Create buffers
//        glGenBuffers(1, vboHandle.ptr)
//        glGenBuffers(1, elementsHandle.ptr)
//
//        createFontsTexture()
//
//        // Restore modified GL state
//        glBindTexture(GL_TEXTURE_2D, lastTexture.value.convert())
//        glBindBuffer(GL_ARRAY_BUFFER, lastArrayBuffer.value.convert())
//        glBindVertexArray(lastVertexArray.value.convert())
//
//        return@memScoped true
//    }
//
//    fun destroyDeviceObjects() {
//        if (vboHandle.value != 0u) glDeleteBuffers(1, vboHandle.ptr)
//        if (elementsHandle.value != 0u) glDeleteBuffers(1, elementsHandle.ptr)
//        vboHandle.value = 0u
//        elementsHandle.value = 0u
//
//        if (shaderHandle != 0u && vertHandle != 0u) glDetachShader(shaderHandle, vertHandle)
//        if (vertHandle != 0u) glDeleteShader(vertHandle)
//        vertHandle = 0u
//
//        if (shaderHandle != 0u && fragHandle != 0u) glDetachShader(shaderHandle, fragHandle)
//        if (fragHandle != 0u) glDeleteShader(fragHandle)
//        fragHandle = 0u
//
//        if (shaderHandle != 0u) glDeleteProgram(shaderHandle)
//        shaderHandle = 0u
//
//        destroyFontsTexture()
//    }
//
//    // MULTI-VIEWPORT / PLATFORM INTERFACE SUPPORT
//
//    fun renderWindow(viewport: CPointer<ImGuiViewport>?, void: COpaquePointer?) = memScoped {
//        if (viewport!!.pointed.Flags and ImGuiViewportFlags_NoRendererClear.convert() == 0) {
//            val clearColor = alloc<ImVec4> { x = 0f; y = 0f; z = 0f; w = 1f }
//            glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w)
//            glClear(GL_COLOR_BUFFER_BIT)
//        }
//        renderDrawData(viewport.pointed.DrawData!!)
//    }
//
//    fun initPlatformInterface() {
//        val platformIo = igGetPlatformIO()!!.pointed
//        platformIo.Renderer_RenderWindow = staticCFunction(::renderWindow)
//    }
//
//    fun shutdownPlatformInterface() {
//        igDestroyPlatformWindows()
//    }
//}
//
//// GLFW Impl
//
//private inline class GlfwClientApi(val value: Int) {
//    companion object {
//        val Unknown = GlfwClientApi(0)
//        val OpenGL = GlfwClientApi(1)
//        val Vulkan = GlfwClientApi(2)
//    }
//}
//
//@Suppress("NOTHING_TO_INLINE")
//private inline operator fun <T : BooleanVarOf<*>> CPointer<T>?.plus(index: Long): CPointer<T>? =
//    interpretCPointer(this.rawValue + index * 1)
//
//@Suppress("NOTHING_TO_INLINE")
//private inline operator fun <T : BooleanVarOf<*>> CPointer<T>?.plus(index: Int): CPointer<T>? =
//    this + index.toLong()
//
//@Suppress("NOTHING_TO_INLINE")
//private inline operator fun CPointer<BooleanVarOf<Boolean>>.set(index: Int, value: Boolean) {
//    (this + index)!!.pointed.value = value
//}
//
//private inline fun <reified T : CVariable> arraySize(array: CPointer<T>): ULong = arrayBytes(array) / sizeOf<T>().convert()
//
//private fun ImVector_ImGuiPlatformMonitor__grow_capacity(receiver: ImVector_ImGuiPlatformMonitor, size: Int): Int = with(receiver) {
//    val newCapacity = if (Capacity != 0) Capacity + Capacity / 2 else 8
//    if (newCapacity > size) newCapacity else size
//}
//
//private fun ImVector_ImGuiPlatformMonitor_resize(receiver: ImVector_ImGuiPlatformMonitor, newSize: Int) {
//    with(receiver) {
//        if (newSize > Capacity)
//            ImVector_ImGuiPlatformMonitor_reserve(this, ImVector_ImGuiPlatformMonitor__grow_capacity(this, newSize))
//        Size = newSize
//    }
//}
//
//private fun ImVector_ImGuiPlatformMonitor_reserve(receiver: ImVector_ImGuiPlatformMonitor, newCapacity: Int) {
//    with(receiver) {
//        if (newCapacity <= Capacity) return
//        val newData = igMemAlloc(newCapacity.toULong() * sizeOf<ImGuiPlatformMonitor>().toULong())
//        if (Data != null) memcpy(newData, Data, Size.toULong()*sizeOf<ImGuiPlatformMonitor>().toULong())
//        igMemFree(Data)
//        Data = newData?.reinterpret()
//        Capacity = newCapacity
//    }
//}
//
//private object GLFW {
//
//    private val scope = Arena()
//
//    private var window: CPointer<GLFWwindow>? = null
//    private var clientApi = GlfwClientApi.Unknown
//    private var time: Double = 0.0
//    private var mouseJustPressed = BooleanArray(5) { false }
//    private var mouseCursors = Array<CPointer<GLFWcursor>?>(ImGuiMouseCursor_COUNT) { null }
//    private var wantUpdateMonitors: Boolean = true
//
//    private var prevUserCallbackMouseButton: GLFWmousebuttonfun? = null
//    private var prevUserCallbackScroll: GLFWscrollfun? = null
//    private var prevUserCallbackKey: GLFWkeyfun? = null
//    private var prevUserCallbackChar: GLFWcharfun? = null
//
//
//    private fun getClipboardText(userData: COpaquePointer?) = glfwGetClipboardString(userData?.reinterpret())
//    private fun setClipboardText(userData: COpaquePointer?, text: CPointer<ByteVar>?) = glfwSetClipboardString(userData?.reinterpret(), text?.toKString())
//
//    fun mouseButtonCallback(window: CPointer<GLFWwindow>?, button: Int, action: Int, mods: Int) {
//        if (prevUserCallbackMouseButton != null && this.window == window)
//            prevUserCallbackMouseButton!!(window, button, action, mods)
//
//        if (action == GLFW_PRESS && button in mouseJustPressed.indices)
//            mouseJustPressed[button] = true
//    }
//
//    fun scrollCallback(window: CPointer<GLFWwindow>?, xOffset: Double, yOffset: Double) {
//        if (prevUserCallbackScroll != null && this.window == window)
//            prevUserCallbackScroll!!(window, xOffset, yOffset)
//
//        val io = igGetIO()!!.pointed
//        io.MouseWheelH += xOffset.toFloat()
//        io.MouseWheel += yOffset.toFloat()
//    }
//
//    fun keyCallback(window: CPointer<GLFWwindow>?, key: Int, scancode: Int, action: Int, mods: Int) {
//        if (prevUserCallbackKey != null && this.window == window)
//            prevUserCallbackKey!!(window, key, scancode, action, mods)
//
//        val io = igGetIO()!!.pointed
//        if (action == GLFW_PRESS)
//            io.KeysDown[key] = true
//        if (action == GLFW_RELEASE)
//            io.KeysDown[key] = false
//
//        // Modifiers are not reliable across systems
//        io.KeyCtrl = io.KeysDown[GLFW_KEY_LEFT_CONTROL].value || io.KeysDown[GLFW_KEY_RIGHT_CONTROL].value
//        io.KeyShift = io.KeysDown[GLFW_KEY_LEFT_SHIFT].value || io.KeysDown[GLFW_KEY_RIGHT_SHIFT].value
//        io.KeyAlt = io.KeysDown[GLFW_KEY_LEFT_ALT].value || io.KeysDown[GLFW_KEY_RIGHT_ALT].value
//        io.KeySuper = io.KeysDown[GLFW_KEY_LEFT_SUPER].value || io.KeysDown[GLFW_KEY_RIGHT_SUPER].value
//    }
//
//    fun charCallback(window: CPointer<GLFWwindow>?, char: UInt) {
//        if (prevUserCallbackChar != null && this.window == window)
//            prevUserCallbackChar!!(window, char)
//
//        val io = igGetIO()!!
//        if (char in 1u..0xFFFFu)
//            ImGuiIO_AddInputCharacter(io, char.toUShort())
//    }
//
//    private fun init(window: CPointer<GLFWwindow>, installCallbacks: Boolean, clientApi: GlfwClientApi): Boolean {
//        this.window = window
//        time = 0.0
//
//        // Setup back-end capabilities flags
//        val io = igGetIO()!!.pointed
//        io.BackendFlags = io.BackendFlags or ImGuiBackendFlags_HasMouseCursors.convert()        // We can honor GetMouseCursor() values (optional)
//        io.BackendFlags = io.BackendFlags or ImGuiBackendFlags_HasSetMousePos.convert()         // We can honor io.WantSetMousePos requests (optional, rarely used)
//        io.BackendFlags = io.BackendFlags or ImGuiBackendFlags_PlatformHasViewports.convert()   // We can create multi-viewports on the Platform side (optional)
//        //#if GLFW_HAS_GLFW_HOVERED && defined(_WIN32)
//        //io.BackendFlags = io.BackendFlags or ImGuiBackendFlags_HasMouseHoveredViewport  // We can set io.MouseHoveredViewport correctly (optional, not easy)
//        //#endif
//        io.BackendPlatformName = "imgui_impl_glfw".cstr.placeTo(scope)
//
//        // Keyboard mapping. ImGui will use those indices to peek into the io.KeysDown[] array.
//        io.KeyMap[ImGuiKey_Tab.value.convert()] = GLFW_KEY_TAB
//        io.KeyMap[ImGuiKey_LeftArrow.value.convert()] = GLFW_KEY_LEFT
//        io.KeyMap[ImGuiKey_RightArrow.value.convert()] = GLFW_KEY_RIGHT
//        io.KeyMap[ImGuiKey_UpArrow.value.convert()] = GLFW_KEY_UP
//        io.KeyMap[ImGuiKey_DownArrow.value.convert()] = GLFW_KEY_DOWN
//        io.KeyMap[ImGuiKey_PageUp.value.convert()] = GLFW_KEY_PAGE_UP
//        io.KeyMap[ImGuiKey_PageDown.value.convert()] = GLFW_KEY_PAGE_DOWN
//        io.KeyMap[ImGuiKey_Home.value.convert()] = GLFW_KEY_HOME
//        io.KeyMap[ImGuiKey_End.value.convert()] = GLFW_KEY_END
//        io.KeyMap[ImGuiKey_Insert.value.convert()] = GLFW_KEY_INSERT
//        io.KeyMap[ImGuiKey_Delete.value.convert()] = GLFW_KEY_DELETE
//        io.KeyMap[ImGuiKey_Backspace.value.convert()] = GLFW_KEY_BACKSPACE
//        io.KeyMap[ImGuiKey_Space.value.convert()] = GLFW_KEY_SPACE
//        io.KeyMap[ImGuiKey_Enter.value.convert()] = GLFW_KEY_ENTER
//        io.KeyMap[ImGuiKey_Escape.value.convert()] = GLFW_KEY_ESCAPE
//        io.KeyMap[ImGuiKey_A.value.convert()] = GLFW_KEY_A
//        io.KeyMap[ImGuiKey_C.value.convert()] = GLFW_KEY_C
//        io.KeyMap[ImGuiKey_V.value.convert()] = GLFW_KEY_V
//        io.KeyMap[ImGuiKey_X.value.convert()] = GLFW_KEY_X
//        io.KeyMap[ImGuiKey_Y.value.convert()] = GLFW_KEY_Y
//        io.KeyMap[ImGuiKey_Z.value.convert()] = GLFW_KEY_Z
//
//        io.SetClipboardTextFn = staticCFunction(::setClipboardText)
//        io.GetClipboardTextFn = staticCFunction(::getClipboardText)
//        io.ClipboardUserData = this.window;
//
//        mouseCursors[ImGuiMouseCursor_Arrow] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR)
//        mouseCursors[ImGuiMouseCursor_TextInput] = glfwCreateStandardCursor(GLFW_IBEAM_CURSOR)
//        mouseCursors[ImGuiMouseCursor_ResizeAll] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR) // FIXME: GLFW doesn't have this.
//        mouseCursors[ImGuiMouseCursor_ResizeNS] = glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR)
//        mouseCursors[ImGuiMouseCursor_ResizeEW] = glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR)
//        mouseCursors[ImGuiMouseCursor_ResizeNESW] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR) // FIXME: GLFW doesn't have this.
//        mouseCursors[ImGuiMouseCursor_ResizeNWSE] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR) // FIXME: GLFW doesn't have this.
//        mouseCursors[ImGuiMouseCursor_Hand] = glfwCreateStandardCursor(GLFW_HAND_CURSOR)
//
//        // Chain GLFW callbacks: our callbacks will call the user's previously installed callbacks, if any.
//        prevUserCallbackMouseButton = null;
//        prevUserCallbackScroll = null;
//        prevUserCallbackKey = null;
//        prevUserCallbackChar = null;
//        if (installCallbacks) {
//            prevUserCallbackMouseButton = glfwSetMouseButtonCallback(window, staticCFunction(::mouseButtonCallback))
//            prevUserCallbackScroll = glfwSetScrollCallback(window, staticCFunction(::scrollCallback))
//            prevUserCallbackKey = glfwSetKeyCallback(window, staticCFunction(::keyCallback))
//            prevUserCallbackChar = glfwSetCharCallback(window, staticCFunction(::charCallback))
//        }
//
//        // Our mouse update function expect PlatformHandle to be filled for the main viewport
//        val mainViewport = igGetMainViewport()!!.pointed
//        mainViewport.PlatformHandle = this.window
//        if (io.ConfigFlags and ImGuiConfigFlags_ViewportsEnable.convert() != 0)
//            initPlatformInterface()
//
//        this.clientApi = clientApi
//        return true
//    }
//
//    fun initForOpenGL(window: CPointer<GLFWwindow>, installCallbacks: Boolean): Boolean {
//        return init(window, installCallbacks, GlfwClientApi.OpenGL)
//    }
//
//    fun initForVulkan(window: CPointer<GLFWwindow>, installCallbacks: Boolean): Boolean {
//        return init(window, installCallbacks, GlfwClientApi.Vulkan)
//    }
//
//    fun shutdown() {
//        shutdownPlatformInterface()
//
//        for (i in 0 until ImGuiMouseCursor_COUNT) {
//            glfwDestroyCursor(mouseCursors[i])
//            mouseCursors[i] = null
//        }
//        clientApi = GlfwClientApi.Unknown
//    }
//
//    private fun updateMousePosAndButtons() = memScoped {
//        val io = igGetIO()!!.pointed
//        val mousePosBackup = io.MousePos.x to io.MousePos.y
//        io.MousePos.apply { x = -Float.MAX_VALUE; y = -Float.MAX_VALUE }
//        io.MouseHoveredViewport = 0u
//
//        // update buttons
//        for (i in 0 until arraySize(io.MouseDown).convert()) {
//            // If a mouse press event came, always pass it as "mouse held this frame", so we don't miss click-release events that are shorter than 1 frame.
//            io.MouseDown[i] = mouseJustPressed[i] || glfwGetMouseButton(this@GLFW.window, i) != 0
//            mouseJustPressed[i] = false
//        }
//
//        val platformIo = igGetPlatformIO()!!.pointed
//        for (n in 0 until platformIo.Viewports.Size) {
//            val viewport = platformIo.Viewports.Data!![n]
//            val window = viewport!!.pointed.PlatformHandle?.reinterpret<GLFWwindow>()
//            Hazel.coreAssert(window != null)
//            val focused = glfwGetWindowAttrib(window, GLFW_FOCUSED) != 0
//            if (focused) {
//                if (io.WantSetMousePos) {
//                    glfwSetCursorPos(
//                        window,
//                        (mousePosBackup.first - viewport.pointed.Pos.x).toDouble(),
//                        (mousePosBackup.second - viewport.pointed.Pos.y).toDouble()
//                    )
//                } else {
//                    val mouseX = alloc<DoubleVar>()
//                    val mouseY = alloc<DoubleVar>()
//                    glfwGetCursorPos(window, mouseX.ptr, mouseY.ptr)
//                    if (io.ConfigFlags and ImGuiConfigFlags_ViewportsEnable.convert() != 0) {
//                        // Multi-viewport mode: mouse position in OS absolute coordinates (io.MousePos is (0,0) when the mouse is on the upper-left of the primary monitor)
//                        val windowX = alloc<IntVar>()
//                        val windowY = alloc<IntVar>()
//                        glfwGetWindowPos(window, windowX.ptr, windowY.ptr)
//                        io.MousePos.apply {
//                            x = mouseX.value.toFloat() + windowX.value
//                            y = mouseY.value.toFloat() + windowY.value
//                        }
//                    } else {
//                        // Single viewport mode: mouse position in client window coordinates (io.MousePos is (0,0) when the mouse is on the upper-left corner of the app window)
//                        io.MousePos.apply { x = mouseX.value.toFloat(); y = mouseY.value.toFloat() }
//                    }
//                }
//                for (i in 0 until arraySize(io.MouseDown).convert())
//                    io.MouseDown[i] = io.MouseDown[i].value or (glfwGetMouseButton(window, i) != 0)
//            }
//            // (Optional) When using multiple viewports: set io.MouseHoveredViewport to the viewport the OS mouse cursor is hovering.
//            // Important: this information is not easy to provide and many high-level windowing library won't be able to provide it correctly, because
//            // - This is _ignoring_ viewports with the ImGuiViewportFlags_NoInputs flag (pass-through windows).
//            // - This is _regardless_ of whether another viewport is focused or being dragged from.
//            // If ImGuiBackendFlags_HasMouseHoveredViewport is not set by the back-end, imgui will ignore this field and infer the information by relying on the
//            // rectangles and last focused time of every viewports it knows about. It will be unaware of other windows that may be sitting between or over your windows.
//            // [GLFW] FIXME: This is currently only correct on Win32. See what we do below with the WM_NCHITTEST, missing an equivalent for other systems.
//            // See https://github.com/glfw/glfw/issues/1236 if you want to help in making this a GLFW feature.
//            /*
//            //#if GLFW_HAS_GLFW_HOVERED && defined(_WIN32)
//            if (glfwGetWindowAttrib(window, GLFW_HOVERED) && !(viewport->Flags & ImGuiViewportFlags_NoInputs))
//            io.MouseHoveredViewport = viewport->ID;
//            //#endif
//             */
//        }
//    }
//
//    private fun updateMouseCursor() {
//        val io = igGetIO()!!.pointed
//        if ((io.ConfigFlags and ImGuiConfigFlags_NoMouseCursorChange.convert() != 0) ||
//            glfwGetInputMode(this.window, GLFW_CURSOR) == GLFW_CURSOR_DISABLED
//        ) {
//            return
//        }
//
//        val imguiCursor = igGetMouseCursor()
//        val platformIO = igGetPlatformIO()!!.pointed
//        for (n in 0 until platformIO.Viewports.Size) {
//            val window = platformIO.Viewports.Data!![n]!!.pointed.PlatformHandle?.reinterpret<GLFWwindow>()
//            if (imguiCursor == ImGuiMouseCursor_None || io.MouseDrawCursor) {
//                // Hide OS mouse cursor if imgui is drawing it or if it wants no cursor
//                glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
//            } else {
//                // Show OS mouse cursor
//                // FIXME-PLATFORM: Unfocused windows seems to fail changing the mouse cursor with GLFW 3.2, but 3.3 works here.
//                glfwSetCursor(window, mouseCursors[imguiCursor] ?: mouseCursors[ImGuiMouseCursor_Arrow])
//                glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
//            }
//        }
//    }
//
//    fun newFrame() = memScoped {
//        val io = igGetIO()!!.pointed
//        Hazel.coreAssert(ImFontAtlas_IsBuilt(io.Fonts))
//
//        // Setup display size
//        val w = alloc<IntVar>()
//        val h = alloc<IntVar>()
//        val displayW = alloc<IntVar>()
//        val displayH = alloc<IntVar>()
//        glfwGetWindowSize(window, w.ptr, h.ptr)
//        glfwGetFramebufferSize(window, displayW.ptr, displayH.ptr)
//        io.DisplaySize.apply { x = w.value.toFloat(); y = h.value.toFloat() }
//        io.DisplayFramebufferScale.apply {
//            x = if (w.value > 0) (displayW.value / w.value).toFloat() else 0f
//            y = if (h.value > 0) (displayH.value / h.value).toFloat() else 0f
//        }
//        if (wantUpdateMonitors) updateMonitors()
//
//        // Setup time step
//        val currentTime = glfwGetTime()
//        io.DeltaTime = if (time > 0.0) (currentTime - time).toFloat() else 1 / 60f
//        time = currentTime
//
//        updateMousePosAndButtons()
//        updateMouseCursor()
//
//        // Gamepad navigation mapping [BETA]
//        memset(io.NavInputs, 0, arrayBytes(io.NavInputs))
//        if (io.ConfigFlags and ImGuiConfigFlags_NavEnableGamepad.convert() != 0) {
//            // Update gamepad inputs
//            val axisCount = alloc<IntVar>()
//            val buttonCount = alloc<IntVar>()
//            val axes = glfwGetJoystickAxes(GLFW_JOYSTICK_1, axisCount.ptr)!!
//            val buttons = glfwGetJoystickButtons(GLFW_JOYSTICK_1, buttonCount.ptr)!!
//
//            fun mapButton(nav: Int, button: Int) {
//                if (buttonCount.value > button && buttons[button] == GLFW_PRESS.convert())
//                    io.NavInputs[nav] = 1.0f
//            }
//
//            fun mapAnalog(nav: Int, axis: Int, v0: Float, v1: Float) {
//                var v = if (axisCount.value > axis) axes[axis] else v0
//                v = (v - v0) / (v1 - v0)
//                if (v > 1.0f) v = 1.0f
//                if (io.NavInputs[nav] < v) io.NavInputs[nav] = v;
//            }
//
//            mapButton(ImGuiNavInput_Activate.convert(), 0)      // Cross / A
//            mapButton(ImGuiNavInput_Cancel.convert(), 1)      // Circle / B
//            mapButton(ImGuiNavInput_Menu.convert(), 2)      // Square / X
//            mapButton(ImGuiNavInput_Input.convert(), 3)      // Triangle / Y
//            mapButton(ImGuiNavInput_DpadLeft.convert(), 13)     // D-Pad Left
//            mapButton(ImGuiNavInput_DpadRight.convert(), 11)     // D-Pad Right
//            mapButton(ImGuiNavInput_DpadUp.convert(), 10)     // D-Pad Up
//            mapButton(ImGuiNavInput_DpadDown.convert(), 12)     // D-Pad Down
//            mapButton(ImGuiNavInput_FocusPrev.convert(), 4)      // L1 / LB
//            mapButton(ImGuiNavInput_FocusNext.convert(), 5)      // R1 / RB
//            mapButton(ImGuiNavInput_TweakSlow.convert(), 4)      // L1 / LB
//            mapButton(ImGuiNavInput_TweakFast.convert(), 5)      // R1 / RB
//            mapAnalog(ImGuiNavInput_LStickLeft.convert(), 0, -0.3f, -0.9f)
//            mapAnalog(ImGuiNavInput_LStickRight.convert(), 0, +0.3f, +0.9f)
//            mapAnalog(ImGuiNavInput_LStickUp.convert(), 1, +0.3f, +0.9f)
//            mapAnalog(ImGuiNavInput_LStickDown.convert(), 1, -0.3f, -0.9f)
//
//            if (axisCount.value > 0 && buttonCount.value > 0)
//                io.BackendFlags = io.BackendFlags or ImGuiBackendFlags_HasGamepad.convert()
//            else
//                io.BackendFlags = io.BackendFlags and ImGuiBackendFlags_HasGamepad.inv().convert()
//        }
//    }
//
//
//    // MULTI-VIEWPORT / PLATFORM INTERFACE SUPPORT
//
//    class ViewportData(
//        var window: CPointer<GLFWwindow>? = null,
//        var windowOwned: Boolean = false
//    )
//
//    private fun windowCloseCallback(window: CPointer<GLFWwindow>?) {
//        igFindViewportByPlatformHandle(window)?.pointed?.PlatformRequestClose = true
//    }
//
//    private fun windowPosCallback(window: CPointer<GLFWwindow>?, x: Int, y: Int) {
//        igFindViewportByPlatformHandle(window)?.pointed?.PlatformRequestMove = true
//    }
//
//    private fun windowSizeCallback(window: CPointer<GLFWwindow>?, w: Int, h: Int) {
//        igFindViewportByPlatformHandle(window)?.pointed?.PlatformRequestResize = true
//    }
//
//    private fun createWindow(viewport: CPointer<ImGuiViewport>?) {
//        val data = ViewportData()
//        viewport!!.pointed.PlatformUserData = StableRef.create(data).asCPointer()
//
//        // GLFW 3.2 unfortunately always set focus on glfwCreateWindow() if GLFW_VISIBLE is set, regardless of GLFW_FOCUSED
//        glfwWindowHint(GLFW_VISIBLE, 0)
//        glfwWindowHint(GLFW_FOCUSED, 0)
//        glfwWindowHint(GLFW_DECORATED, if (viewport.pointed.Flags and ImGuiViewportFlags_NoDecoration.convert() != 0) 0 else 1)
//        //#if GLFW_HAS_WINDOW_TOPMOST
//        //glfwWindowHint(GLFW_FLOATING, (viewport->Flags & ImGuiViewportFlags_TopMost) ? true : false);
//        //#endif
//        val shareWindow = if (clientApi == GlfwClientApi.OpenGL) window else null
//        data.window = glfwCreateWindow(viewport.pointed.Size.x.toInt(), viewport.pointed.Size.y.toInt(), "No Title Yet", null, shareWindow)
//        data.windowOwned = true
//        viewport.pointed.PlatformHandle = data.window
//        glfwSetWindowPos(data.window, viewport.pointed.Pos.x.toInt(), viewport.pointed.Pos.y.toInt())
//
//        // Install callbacks for secondary viewports
//        glfwSetMouseButtonCallback(data.window, staticCFunction(::mouseButtonCallback))
//        glfwSetScrollCallback(data.window, staticCFunction(::scrollCallback))
//        glfwSetKeyCallback(data.window, staticCFunction(::keyCallback))
//        glfwSetCharCallback(data.window, staticCFunction(::charCallback))
//        glfwSetWindowCloseCallback(data.window, staticCFunction(::windowCloseCallback))
//        glfwSetWindowPosCallback(data.window, staticCFunction(::windowPosCallback))
//        glfwSetWindowSizeCallback(data.window, staticCFunction(::windowSizeCallback))
//
//        if (clientApi == GlfwClientApi.OpenGL) {
//            glfwMakeContextCurrent(data.window)
//            glfwSwapInterval(0)
//        }
//    }
//
//    private fun destroyWindow(viewport: CPointer<ImGuiViewport>?) {
//        viewport!!.pointed.PlatformUserData?.asStableRef<ViewportData>()?.let { data ->
//            if (data.get().windowOwned) {
//                //#if GLFW_HAS_GLFW_HOVERED && defined(_WIN32)
//                //HWND hwnd = glfwGetWin32Window(data->Window)
//                //::RemovePropA(hwnd, "IMGUI_VIEWPORT")
//                //#endif
//                glfwDestroyWindow(data.get().window)
//            }
//            data.get().window = null
//            data.dispose()
//        }
//        viewport.pointed.PlatformHandle = null
//        viewport.pointed.PlatformUserData = null
//    }
//
//    //#if defined(_WIN32) && GLFW_HAS_GLFW_HOVERED
//    //static WNDPROC g_GlfwWndProc = NULL;
//    //static LRESULT CALLBACK WndProcNoInputs(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam)
//    //{
//    //    if (msg == WM_NCHITTEST)
//    //    {
//    //        // Let mouse pass-through the window. This will allow the back-end to set io.MouseHoveredViewport properly (which is OPTIONAL).
//    //        // The ImGuiViewportFlags_NoInputs flag is set while dragging a viewport, as want to detect the window behind the one we are dragging.
//    //        // If you cannot easily access those viewport flags from your windowing/event code: you may manually synchronize its state e.g. in
//    //        // your main loop after calling UpdatePlatformWindows(). Iterate all viewports/platform windows and pass the flag to your windowing system.
//    //        ImGuiViewport* viewport = (ImGuiViewport*)::GetPropA(hWnd, "IMGUI_VIEWPORT");
//    //        if (viewport->Flags & ImGuiViewportFlags_NoInputs)
//    //        return HTTRANSPARENT;
//    //    }
//    //    return ::CallWindowProc(g_GlfwWndProc, hWnd, msg, wParam, lParam);
//    //}
//    //#endif
//
//    private fun showWindow(viewport: CPointer<ImGuiViewport>?) {
//        val data = viewport!!.pointed.PlatformUserData!!.asStableRef<ViewportData>().get()
//
//        //#if defined(_WIN32)
//        //// GLFW hack: Hide icon from task bar
//        //HWND hwnd = glfwGetWin32Window (data->Window);
//        //if (viewport->Flags & ImGuiViewportFlags_NoTaskBarIcon)
//        //{
//        //    LONG ex_style =::GetWindowLong(hwnd, GWL_EXSTYLE);
//        //    ex_style & = ~WS_EX_APPWINDOW;
//        //    ex_style | = WS_EX_TOOLWINDOW;
//        //    ::SetWindowLong(hwnd, GWL_EXSTYLE, ex_style);
//        //}
//        //
//        //// GLFW hack: install hook for WM_NCHITTEST message handler
//        //#if GLFW_HAS_GLFW_HOVERED && defined(_WIN32)
//        //::SetPropA(hwnd, "IMGUI_VIEWPORT", viewport);
//        //if (g_GlfwWndProc == NULL)
//        //    g_GlfwWndProc = (WNDPROC)::GetWindowLongPtr(hwnd, GWLP_WNDPROC);
//        //::SetWindowLongPtr(hwnd, GWLP_WNDPROC, (LONG_PTR) WndProcNoInputs);
//        //#endif
//        //
//        //// GLFW hack: GLFW 3.2 has a bug where glfwShowWindow() also activates/focus the window.
//        //// The fix was pushed to GLFW repository on 2018/01/09 and should be included in GLFW 3.3. See https://github.com/glfw/glfw/issues/1179
//        //if (viewport->Flags & ImGuiViewportFlags_NoFocusOnAppearing)
//        //{
//        //    ::ShowWindow(hwnd, SW_SHOWNA);
//        //    return;
//        //}
//        //#endif
//
//        glfwShowWindow(data.window)
//    }
//
//    private fun getWindowPosition(viewport: CPointer<ImGuiViewport>?) = memScoped {
//        val data = viewport!!.pointed.PlatformUserData!!.asStableRef<ViewportData>().get()
//        val x = alloc<IntVar>()
//        val y = alloc<IntVar>()
//        glfwGetWindowPos(data.window, x.ptr, y.ptr)
//        x.value.toFloat() to y.value.toFloat()
//    }
//
//    private fun setWindowPosition(viewport: CPointer<ImGuiViewport>?, position: Pair<Float, Float>) {
//        val data = viewport!!.pointed.PlatformUserData!!.asStableRef<ViewportData>().get()
//        glfwSetWindowPos(data.window, position.first.toInt(), position.second.toInt())
//    }
//
//    static ImVec2 ImGui_ImplGlfw_GetWindowSize(ImGuiViewport* viewport)
//    {
//        ImGuiViewportDataGlfw * data = (ImGuiViewportDataGlfw *) viewport->PlatformUserData;
//        int w = 0, h = 0;
//        glfwGetWindowSize(data->Window, &w, &h);
//        return ImVec2((float) w, (float) h);
//    }
//
//    static void ImGui_ImplGlfw_SetWindowSize(ImGuiViewport* viewport, ImVec2 size)
//    {
//        ImGuiViewportDataGlfw * data = (ImGuiViewportDataGlfw *) viewport->PlatformUserData;
//        glfwSetWindowSize(data->Window, (int)size.x, (int)size.y);
//    }
//
//    static void ImGui_ImplGlfw_SetWindowTitle(ImGuiViewport* viewport, const char* title)
//    {
//        ImGuiViewportDataGlfw * data = (ImGuiViewportDataGlfw *) viewport->PlatformUserData;
//        glfwSetWindowTitle(data->Window, title);
//    }
//
//    static void ImGui_ImplGlfw_SetWindowFocus(ImGuiViewport* viewport)
//    {
//        #if GLFW_HAS_FOCUS_WINDOW
//        ImGuiViewportDataGlfw * data = (ImGuiViewportDataGlfw *) viewport->PlatformUserData;
//        glfwFocusWindow(data->Window);
//        #else
//        // FIXME: What are the effect of not having this function? At the moment imgui doesn't actually call SetWindowFocus - we set that up ahead, will answer that question later.
//        (void) viewport;
//        #endif
//    }
//
//    static bool ImGui_ImplGlfw_GetWindowFocus(ImGuiViewport* viewport)
//    {
//        ImGuiViewportDataGlfw * data = (ImGuiViewportDataGlfw *) viewport->PlatformUserData;
//        return glfwGetWindowAttrib(data->Window, GLFW_FOCUSED) != 0;
//    }
//
//    static bool ImGui_ImplGlfw_GetWindowMinimized(ImGuiViewport* viewport)
//    {
//        ImGuiViewportDataGlfw * data = (ImGuiViewportDataGlfw *) viewport->PlatformUserData;
//        return glfwGetWindowAttrib(data->Window, GLFW_ICONIFIED) != 0;
//    }
//
//    #if GLFW_HAS_WINDOW_ALPHA
//    static void ImGui_ImplGlfw_SetWindowAlpha(ImGuiViewport* viewport, float alpha)
//    {
//        ImGuiViewportDataGlfw * data = (ImGuiViewportDataGlfw *) viewport->PlatformUserData;
//        glfwSetWindowOpacity(data->Window, alpha);
//    }
//    #endif
//
//    static void ImGui_ImplGlfw_RenderWindow(ImGuiViewport* viewport, void*)
//    {
//        ImGuiViewportDataGlfw * data = (ImGuiViewportDataGlfw *) viewport->PlatformUserData;
//        if (g_ClientApi == GlfwClientApi_OpenGL)
//            glfwMakeContextCurrent(data->Window);
//    }
//
//    static void ImGui_ImplGlfw_SwapBuffers(ImGuiViewport* viewport, void*)
//    {
//        ImGuiViewportDataGlfw * data = (ImGuiViewportDataGlfw *) viewport->PlatformUserData;
//        if (g_ClientApi == GlfwClientApi_OpenGL)
//            glfwSwapBuffers(data->Window);
//    }
//
////--------------------------------------------------------------------------------------------------------
//// IME (Input Method Editor) basic support for e.g. Asian language users
////--------------------------------------------------------------------------------------------------------
//
//// We provide a Win32 implementation because this is such a common issue for IME users
//    #if defined(_WIN32) && !defined(IMGUI_DISABLE_WIN32_FUNCTIONS) && !defined(IMGUI_DISABLE_WIN32_DEFAULT_IME_FUNCTIONS) && !defined(__GNUC__)
//    #define HAS_WIN32_IME   1
//    #include <imm.h>
//    #ifdef _MSC_VER
//    #pragma comment(lib, "imm32")
//    #endif
//    static void ImGui_ImplWin32_SetImeInputPos(ImGuiViewport* viewport, ImVec2 pos)
//    {
//        COMPOSITIONFORM cf = { CFS_FORCE_POSITION, { (LONG)(pos.x - viewport->Pos.x), (LONG)(pos.y-viewport->Pos.y) }, { 0, 0, 0, 0 } };
//        if (ImGuiViewportDataGlfw * data = (ImGuiViewportDataGlfw *) viewport->PlatformUserData)
//        if (HWND hwnd = glfwGetWin32Window (data->Window))
//        if (HIMC himc =::ImmGetContext(hwnd)) {
//            ::ImmSetCompositionWindow(himc, & cf);
//            ::ImmReleaseContext(hwnd, himc);
//        }
//    }
//    #else
//    #define HAS_WIN32_IME   0
//    #endif
//
////--------------------------------------------------------------------------------------------------------
//// Vulkan support (the Vulkan renderer needs to call a platform-side support function to create the surface)
////--------------------------------------------------------------------------------------------------------
//
//// Avoid including <vulkan.h> so we can build without it
//    #if GLFW_HAS_VULKAN
//    #ifndef VULKAN_H_
//    #define VK_DEFINE_HANDLE(
//    object) typedef struct
//    object##_T*
//    object;
//    #if defined(__LP64__) || defined(_WIN64) || defined(__x86_64__) || defined(_M_X64) || defined(__ia64) || defined (_M_IA64) || defined(__aarch64__) || defined(__powerpc64__)
//    #define VK_DEFINE_NON_DISPATCHABLE_HANDLE(
//    object) typedef struct
//    object##_T *
//    object;
//    #else
//    #define VK_DEFINE_NON_DISPATCHABLE_HANDLE(
//    object) typedef uint64_t
//    object;
//    #endif
//    VK_DEFINE_HANDLE(VkInstance)
//    VK_DEFINE_NON_DISPATCHABLE_HANDLE(VkSurfaceKHR)
//    struct VkAllocationCallbacks;
//    enum VkResult { VK_RESULT_MAX_ENUM = 0x7FFFFFFF };
//    #endif // VULKAN_H_
//    extern "C"
//    { extern GLFWAPI VkResult glfwCreateWindowSurface (VkInstance instance, GLFWwindow* window, const VkAllocationCallbacks* allocator, VkSurfaceKHR* surface); }
//    static int ImGui_ImplGlfw_CreateVkSurface(ImGuiViewport* viewport, ImU64 vk_instance, const void* vk_allocator, ImU64* out_vk_surface)
//    {
//        ImGuiViewportDataGlfw * data = (ImGuiViewportDataGlfw *) viewport->PlatformUserData;
//        IM_ASSERT(g_ClientApi == GlfwClientApi_Vulkan);
//        VkResult err = glfwCreateWindowSurface ((VkInstance) vk_instance, data->Window, (const VkAllocationCallbacks*)vk_allocator, (VkSurfaceKHR*)out_vk_surface);
//        return (int) err;
//    }
//    #endif // GLFW_HAS_VULKAN
//
//    // FIXME-PLATFORM: GLFW doesn't export monitor work area (see https://github.com/glfw/glfw/pull/989)
//    private fun updateMontors() = memScoped {
//        val platformIo = igGetPlatformIO()!!.pointed
//        val monitorCount = alloc<IntVar>()
//        val glfwMonitors = glfwGetMonitors(monitorCount.ptr)!!
//        ImVector_ImGuiPlatformMonitor_resize(platformIo.Monitors, 0)
//        for (n in 0 until monitorCount.value) {
//            val monitor = alloc<ImGuiPlatformMonitor>()
//            val x = alloc<IntVar>()
//            val y = alloc<IntVar>()
//            glfwGetMonitorPos(glfwMonitors[n], x.ptr, y.ptr)
//            val vidMode = glfwGetVideoMode (glfwMonitors[n])!!.pointed
//            monitor.MainPos.apply { this.x = x.value.toFloat(); this.y = y.value.toFloat() }
//            monitor.WorkPos.apply { this.x = x.value.toFloat(); this.y = y.value.toFloat() }
//            monitor.MainSize.apply { this.x = vidMode.width.toFloat(); this.y = vidMode.height.toFloat() }
//            monitor.WorkSize.apply { this.x = vidMode.width.toFloat(); this.y = vidMode.height.toFloat() }
//            //#if GLFW_HAS_PER_MONITOR_DPI
//            //// Warning: the validity of monitor DPI information on Windows depends on the application DPI awareness settings, which generally needs to be set in the manifest or at runtime.
//            //float x_scale, y_scale;
//            //glfwGetMonitorContentScale(glfw_monitors[n], & x_scale, &y_scale);
//            //monitor.DpiScale = x_scale;
//            //#endif
//            ImVector_ImGuiPlatformMonitor_push_back(platformIo.Monitors, monitor)
//        }
//        wantUpdateMonitors = false
//    }
//
//
//    private fun monitorCallback(monitor: CPointer<GLFWmonitor>?, n: Int) {
//        wantUpdateMonitors = true
//    }
//
//    private fun initPlatformInterface() {
//        // Register platform interface (will be coupled with a renderer interface)
//        val platformIo = igGetPlatformIO()!!.pointed
//        platformIo.Platform_CreateWindow = staticCFunction(::createWindow)
//        platformIo.Platform_DestroyWindow = staticCFunction(::destroyWindow)
//        platformIo.Platform_ShowWindow = staticCFunction(::showWindow)
//        platformIo.Platform_SetWindowPos = staticCFunction(::setWindowPos)
//        platformIo.Platform_GetWindowPos = staticCFunction(::getWindowPos)
//        platformIo.Platform_SetWindowSize = staticCFunction(::setWindowSize)
//        platformIo.Platform_GetWindowSize = staticCFunction(::getWindowSize)
//        platformIo.Platform_SetWindowFocus = staticCFunction(::setWindowFocus)
//        platformIo.Platform_GetWindowFocus = staticCFunction(::getWindowFocus)
//        platformIo.Platform_GetWindowMinimized = staticCFunction(::getWindowMinimized)
//        platformIo.Platform_SetWindowTitle = staticCFunction(::setWindowTitle)
//        platformIo.Platform_RenderWindow = staticCFunction(::renderWindow)
//        platformIo.Platform_SwapBuffers = staticCFunction(::swapBuffers)
//        //#if GLFW_HAS_WINDOW_ALPHA
//        //platform_io.Platform_SetWindowAlpha = ImGui_ImplGlfw_SetWindowAlpha;
//        //#endif
//        //#if GLFW_HAS_VULKAN
//        //platform_io.Platform_CreateVkSurface = ImGui_ImplGlfw_CreateVkSurface;
//        //#endif
//        //#if HAS_WIN32_IME
//        //platform_io.Platform_SetImeInputPos = ImGui_ImplWin32_SetImeInputPos;
//        //#endif
//
//        // Note: monitor callback are broken GLFW 3.2 and earlier (see github.com/glfw/glfw/issues/784)
//        updateMonitors()
//        glfwSetMonitorCallback(staticCFunction(::monitorCallback))
//
//        // Register main window handle (which is owned by the main application, not by us)
//        val mainViewport = igGetMainViewport()!!.pointed
//        val data = ViewportData(window)
//        mainViewport.PlatformUserData = StableRef.create(data).asCPointer()
//        mainViewport.PlatformHandle = window
//    }
//
//    private fun shutdownPlatformInterface() {}
//}
