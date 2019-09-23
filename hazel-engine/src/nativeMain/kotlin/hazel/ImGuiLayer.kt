package hazel

import cimgui.ImDrawData
import cimgui.ImDrawIdxVar
import cimgui.ImDrawVert
import cimgui.ImDrawVert_col_offset
import cimgui.ImDrawVert_pos_offset
import cimgui.ImDrawVert_uv_offset
import cimgui.ImFontAtlas_GetTexDataAsRGBA32
import cimgui.ImGuiBackendFlags_HasMouseCursors
import cimgui.ImGuiBackendFlags_HasSetMousePos
import cimgui.ImGuiIO_AddInputCharacter
import cimgui.ImGuiKey_.*
import cimgui.ImVec4
import cimgui.igCreateContext
import cimgui.igGetDrawData
import cimgui.igGetIO
import cimgui.igNewFrame
import cimgui.igRender
import cimgui.igShowDemoWindow
import cimgui.igStyleColorsDark
import com.kgl.opengl.GL_ACTIVE_TEXTURE
import com.kgl.opengl.GL_ARRAY_BUFFER
import com.kgl.opengl.GL_ARRAY_BUFFER_BINDING
import com.kgl.opengl.GL_BLEND
import com.kgl.opengl.GL_BLEND_DST_ALPHA
import com.kgl.opengl.GL_BLEND_DST_RGB
import com.kgl.opengl.GL_BLEND_EQUATION_ALPHA
import com.kgl.opengl.GL_BLEND_EQUATION_RGB
import com.kgl.opengl.GL_BLEND_SRC_ALPHA
import com.kgl.opengl.GL_BLEND_SRC_RGB
import com.kgl.opengl.GL_CLIP_ORIGIN
import com.kgl.opengl.GL_COMPILE_STATUS
import com.kgl.opengl.GL_CULL_FACE
import com.kgl.opengl.GL_CURRENT_PROGRAM
import com.kgl.opengl.GL_DEPTH_TEST
import com.kgl.opengl.GL_ELEMENT_ARRAY_BUFFER
import com.kgl.opengl.GL_FALSE
import com.kgl.opengl.GL_FILL
import com.kgl.opengl.GL_FLOAT
import com.kgl.opengl.GL_FRAGMENT_SHADER
import com.kgl.opengl.GL_FRONT_AND_BACK
import com.kgl.opengl.GL_FUNC_ADD
import com.kgl.opengl.GL_INFO_LOG_LENGTH
import com.kgl.opengl.GL_LINEAR
import com.kgl.opengl.GL_LINK_STATUS
import com.kgl.opengl.GL_ONE_MINUS_SRC_ALPHA
import com.kgl.opengl.GL_POLYGON_MODE
import com.kgl.opengl.GL_RGBA
import com.kgl.opengl.GL_SAMPLER_BINDING
import com.kgl.opengl.GL_SCISSOR_BOX
import com.kgl.opengl.GL_SCISSOR_TEST
import com.kgl.opengl.GL_SRC_ALPHA
import com.kgl.opengl.GL_STREAM_DRAW
import com.kgl.opengl.GL_TEXTURE0
import com.kgl.opengl.GL_TEXTURE_2D
import com.kgl.opengl.GL_TEXTURE_BINDING_2D
import com.kgl.opengl.GL_TEXTURE_MAG_FILTER
import com.kgl.opengl.GL_TEXTURE_MIN_FILTER
import com.kgl.opengl.GL_TRIANGLES
import com.kgl.opengl.GL_TRUE
import com.kgl.opengl.GL_UNPACK_ROW_LENGTH
import com.kgl.opengl.GL_UNSIGNED_BYTE
import com.kgl.opengl.GL_UNSIGNED_SHORT
import com.kgl.opengl.GL_UPPER_LEFT
import com.kgl.opengl.GL_VERTEX_SHADER
import com.kgl.opengl.GL_VIEWPORT
import com.kgl.opengl.glActiveTexture
import com.kgl.opengl.glAttachShader
import com.kgl.opengl.glBindBuffer
import com.kgl.opengl.glBindSampler
import com.kgl.opengl.glBindTexture
import com.kgl.opengl.glBindVertexArray
import com.kgl.opengl.glBlendEquation
import com.kgl.opengl.glBlendEquationSeparate
import com.kgl.opengl.glBlendFunc
import com.kgl.opengl.glBlendFuncSeparate
import com.kgl.opengl.glBufferData
import com.kgl.opengl.glCompileShader
import com.kgl.opengl.glCreateProgram
import com.kgl.opengl.glCreateShader
import com.kgl.opengl.glDeleteBuffers
import com.kgl.opengl.glDeleteProgram
import com.kgl.opengl.glDeleteShader
import com.kgl.opengl.glDeleteTextures
import com.kgl.opengl.glDetachShader
import com.kgl.opengl.glDisable
import com.kgl.opengl.glDrawElements
import com.kgl.opengl.glEnable
import com.kgl.opengl.glEnableVertexAttribArray
import com.kgl.opengl.glGenBuffers
import com.kgl.opengl.glGenTextures
import com.kgl.opengl.glGenVertexArrays
import com.kgl.opengl.glGetAttribLocation
import com.kgl.opengl.glGetIntegerv
import com.kgl.opengl.glGetProgramInfoLog
import com.kgl.opengl.glGetProgramiv
import com.kgl.opengl.glGetShaderInfoLog
import com.kgl.opengl.glGetShaderiv
import com.kgl.opengl.glGetUniformLocation
import com.kgl.opengl.glIsEnabled
import com.kgl.opengl.glLinkProgram
import com.kgl.opengl.glPixelStorei
import com.kgl.opengl.glPolygonMode
import com.kgl.opengl.glScissor
import com.kgl.opengl.glShaderSource
import com.kgl.opengl.glTexImage2D
import com.kgl.opengl.glTexParameteri
import com.kgl.opengl.glUniform1i
import com.kgl.opengl.glUniformMatrix4fv
import com.kgl.opengl.glUseProgram
import com.kgl.opengl.glVertexAttribPointer
import com.kgl.opengl.glViewport
import copengl.GLenumVar
import copengl.GLintVar
import copengl.GLuint
import copengl.GLuintVar
import kotlinx.cinterop.Arena
import kotlinx.cinterop.BooleanVar
import kotlinx.cinterop.CPointed
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CPointerVar
import kotlinx.cinterop.FloatVar
import kotlinx.cinterop.IntVar
import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.UIntVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.convert
import kotlinx.cinterop.cstr
import kotlinx.cinterop.get
import kotlinx.cinterop.invoke
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.placeTo
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.set
import kotlinx.cinterop.toCPointer
import kotlinx.cinterop.toCStringArray
import kotlinx.cinterop.toLong
import kotlinx.cinterop.value
import platform.posix.size_tVar
import platform.posix.sscanf
import kotlin.native.OsFamily.MACOSX


// opengl data

private val scope = Arena()

private var glslVersionString: String = ""

private var fontTexture = scope.alloc<GLuintVar> { value = 0u }

private var shaderHandle: GLuint = 0u
private var vertHandle: GLuint = 0u
private var fragHandle: GLuint = 0u

private var attribLocationTex: Int = 0
private var attribLocationProjMtx: Int = 0

private var attribLocationPosition: Int = 0
private var attribLocationUV: Int = 0
private var attribLocationColor: Int = 0

private var vboHandle = scope.alloc<UIntVar> { value = 0u }
private var elementsHandle = scope.alloc<UIntVar> { value = 0u }


// functions

private fun igOpenGL3Init(glslVersion: String? = null): Boolean {
    val io = igGetIO()
    io!!.pointed.BackendRendererName = "imgui_impl_opengl3".cstr.placeTo(scope)

    // Store GLSL version string so we can refer to it later in case we recreate shaders. Note: GLSL version is NOT the same as GL version. Leave this to NULL if unsure.
    glslVersionString = glslVersion ?: "#version 130"

    return true
}

private fun igOpenGL3Shutdown() {
    igOpenGL3DestroyDeviceObjects()
    scope.clear()
}

private fun igOpenGL3NewFrame() {
    if (fontTexture.value == 0u)
        igOpenGL3CreateDeviceObjects()
}

private fun igOpenGL3RenderDrawData(draw_data: CPointer<ImDrawData>): Unit = memScoped {
    // Avoid rendering when minimized, scale coordinates for retina displays (screen coordinates != framebuffer coordinates)
    val fbWidth = (draw_data.pointed.DisplaySize.x * draw_data.pointed.FramebufferScale.x).toInt()
    val fbHeight = (draw_data.pointed.DisplaySize.y * draw_data.pointed.FramebufferScale.y).toInt()
    if (fbWidth <= 0 || fbHeight <= 0)
        return

    // Backup GL state
    val lastActiveTexture = alloc<GLenumVar>(); glGetIntegerv(GL_ACTIVE_TEXTURE, lastActiveTexture.ptr.reinterpret())
    glActiveTexture(GL_TEXTURE0)
    val lastProgram = alloc<GLintVar>(); glGetIntegerv(GL_CURRENT_PROGRAM, lastProgram.ptr)
    val lastTexture = alloc<GLintVar>(); glGetIntegerv(GL_TEXTURE_BINDING_2D, lastTexture.ptr)
//    #ifdef GL_SAMPLER_BINDING
    val lastSampler = alloc<GLintVar>(); glGetIntegerv(GL_SAMPLER_BINDING, lastSampler.ptr)
//    #endif
    val lastArrayBuffer = alloc<GLintVar>(); glGetIntegerv(GL_ARRAY_BUFFER_BINDING, lastArrayBuffer.ptr)
//    #ifdef GL_POLYGON_MODE
    val lastPolygonMode = allocArray<GLintVar>(2); glGetIntegerv(GL_POLYGON_MODE, lastPolygonMode)
//    #endif
    val lastViewport = allocArray<GLintVar>(4); glGetIntegerv(GL_VIEWPORT, lastViewport)
    val lastScissorBox = allocArray<GLintVar>(4); glGetIntegerv(GL_SCISSOR_BOX, lastScissorBox)
    val lastBlendSrcRgb = alloc<GLenumVar>(); glGetIntegerv(GL_BLEND_SRC_RGB, lastBlendSrcRgb.ptr.reinterpret())
    val lastBlendDstRgb = alloc<GLenumVar>(); glGetIntegerv(GL_BLEND_DST_RGB, lastBlendDstRgb.ptr.reinterpret())
    val lastBlendSrcAlpha = alloc<GLenumVar>(); glGetIntegerv(GL_BLEND_SRC_ALPHA, lastBlendSrcAlpha.ptr.reinterpret())
    val lastBlendDstAlpha = alloc<GLenumVar>(); glGetIntegerv(GL_BLEND_DST_ALPHA, lastBlendDstAlpha.ptr.reinterpret())
    val lastBlendEquationRgb = alloc<GLenumVar>(); glGetIntegerv(GL_BLEND_EQUATION_RGB, lastBlendEquationRgb.ptr.reinterpret())
    val lastBlendEquationAlpha = alloc<GLenumVar>(); glGetIntegerv(GL_BLEND_EQUATION_ALPHA, lastBlendEquationAlpha.ptr.reinterpret())
    val lastEnableBlend = glIsEnabled(GL_BLEND)
    val lastEnableCullFace = glIsEnabled(GL_CULL_FACE)
    val lastEnableDepthTest = glIsEnabled(GL_DEPTH_TEST)
    val lastEnableScissorTest = glIsEnabled(GL_SCISSOR_TEST)
    var clipOriginLowerLeft = true
//    #if defined(GL_CLIP_ORIGIN) && !defined(__APPLE__)
    if (Platform.osFamily != MACOSX) {
        val lastClipOrigin = alloc<GLenumVar> { value = 0u }; glGetIntegerv(GL_CLIP_ORIGIN, lastClipOrigin.ptr.reinterpret()) // Support for GL 4.5's glClipControl(GL_UPPER_LEFT)
        if (lastClipOrigin.value == GL_UPPER_LEFT)
            clipOriginLowerLeft = false
    }
//    #endif

    // Setup render state: alpha-blending enabled, no face culling, no depth testing, scissor enabled, polygon fill
    glEnable(GL_BLEND)
    glBlendEquation(GL_FUNC_ADD)
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
    glDisable(GL_CULL_FACE)
    glDisable(GL_DEPTH_TEST)
    glEnable(GL_SCISSOR_TEST)
//    #ifdef GL_POLYGON_MODE
    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)
//    #endif


    // Setup viewport, orthographic projection matrix
    // Our visible imgui space lies from draw_data->DisplayPos (top left) to draw_data->DisplayPos+data_data->DisplaySize (bottom right). DisplayMin is typically (0,0) for single viewport apps.
    glViewport(0, 0, fbWidth, fbHeight)
    val l = draw_data.pointed.DisplayPos.x
    val r = draw_data.pointed.DisplayPos.x + draw_data.pointed.DisplaySize.x
    val t = draw_data.pointed.DisplayPos.y
    val b = draw_data.pointed.DisplayPos.y + draw_data.pointed.DisplaySize.y
    val orthoProjection = allocArray<FloatVar>(16)
    orthoProjection[0] = 2f / (r - l)
    orthoProjection[1] = 0f
    orthoProjection[2] = 0f
    orthoProjection[3] = 0f

    orthoProjection[4] = 0f
    orthoProjection[5] = 2f / (t - b)
    orthoProjection[6] = 0f
    orthoProjection[7] = 0f

    orthoProjection[8] = 0f
    orthoProjection[9] = 0f
    orthoProjection[10] = -1f
    orthoProjection[11] = 0f

    orthoProjection[12] = (r + l) / (l - r)
    orthoProjection[13] = (t + b) / (b - t)
    orthoProjection[14] = 0f
    orthoProjection[15] = 1f

    glUseProgram(shaderHandle)
    glUniform1i(attribLocationTex, 0)
    glUniformMatrix4fv(attribLocationProjMtx, 1, GL_FALSE.convert(), orthoProjection)
//    #ifdef GL_SAMPLER_BINDING
    glBindSampler(0u, 0u) // We use combined texture/sampler state. Applications using GL 3.3 may set that otherwise.
//    #endif

//    #ifndef IMGUI_IMPL_OPENGL_ES2
    // Recreate the VAO every time
    // (This is to easily allow multiple GL contexts. VAO are not shared among GL contexts, and we don't track creation/deletion of windows so we don't have an obvious key to use to cache them.)
    val vaoHandle = alloc<GLuintVar>()
    glGenVertexArrays(1, vaoHandle.ptr)
    glBindVertexArray(vaoHandle.value)
//    #endif
    glBindBuffer(GL_ARRAY_BUFFER, vboHandle.value)
    glEnableVertexAttribArray(attribLocationPosition.convert())
    glEnableVertexAttribArray(attribLocationUV.convert())
    glEnableVertexAttribArray(attribLocationColor.convert())
    glVertexAttribPointer(attribLocationPosition.convert(), 2, GL_FLOAT, GL_FALSE.convert(), ImDrawVert.size.convert(), ImDrawVert_pos_offset())
    glVertexAttribPointer(attribLocationUV.convert(), 2, GL_FLOAT, GL_FALSE.convert(), ImDrawVert.size.convert(), ImDrawVert_uv_offset())
    glVertexAttribPointer(attribLocationColor.convert(), 4, GL_UNSIGNED_BYTE, GL_TRUE.convert(), ImDrawVert.size.convert(), ImDrawVert_col_offset())

    // Will project scissor/clipping rectangles into framebuffer space
    val clipOffset = draw_data.pointed.DisplayPos       // (0,0) unless using multi-viewports
    val clipScale = draw_data.pointed.FramebufferScale  // (1,1) unless using retina display which are often (2,2)

    // Render command lists
    for (n in 0 until draw_data.pointed.CmdListsCount) {
        val cmdList = draw_data.pointed.CmdLists!![n]
        val idxBufferOffset = alloc<size_tVar> { value = 0u }

        glBindBuffer(GL_ARRAY_BUFFER, vboHandle.value)
        glBufferData(GL_ARRAY_BUFFER, cmdList!!.pointed.VtxBuffer.Size * ImDrawVert.size, cmdList.pointed.VtxBuffer.Data, GL_STREAM_DRAW)

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementsHandle.value)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, cmdList.pointed.IdxBuffer.Size * ImDrawIdxVar.size, cmdList.pointed.IdxBuffer.Data, GL_STREAM_DRAW)

        for (cmd_i in 0 until cmdList.pointed.CmdBuffer.Size) {
            val pcmd = cmdList.pointed.CmdBuffer.Data!![cmd_i].ptr
            if (pcmd.pointed.UserCallback != null) {
                // User callback (registered via ImDrawList::AddCallback)
                pcmd.pointed.UserCallback!!(cmdList, pcmd)

            } else {
                // Project scissor/clipping rectangles into framebuffer space
                val clipRect = alloc<ImVec4>()
                clipRect.x = (pcmd.pointed.ClipRect.x - clipOffset.x) * clipScale.x
                clipRect.y = (pcmd.pointed.ClipRect.y - clipOffset.y) * clipScale.y
                clipRect.z = (pcmd.pointed.ClipRect.z - clipOffset.x) * clipScale.x
                clipRect.w = (pcmd.pointed.ClipRect.w - clipOffset.y) * clipScale.y

                if (clipRect.x < fbWidth && clipRect.y < fbHeight && clipRect.z >= 0.0f && clipRect.w >= 0.0f) {
                    // Apply scissor/clipping rectangle
                    if (clipOriginLowerLeft)
                        glScissor(clipRect.x.toInt(), (fbHeight - clipRect.w).toInt(), (clipRect.z - clipRect.x).toInt(), (clipRect.w - clipRect.y).toInt())
                    else
                        glScissor(clipRect.x.toInt(), clipRect.y.toInt(), clipRect.z.toInt(), clipRect.w.toInt()) // Support for GL 4.5's glClipControl(GL_UPPER_LEFT)

                    // Bind texture, Draw
                    glBindTexture(GL_TEXTURE_2D, pcmd.pointed.TextureId.toLong().toUInt())
                    glDrawElements(GL_TRIANGLES, pcmd.pointed.ElemCount.toInt(), GL_UNSIGNED_SHORT, idxBufferOffset.value.toLong().toCPointer<CPointed>())
                }
            }
            idxBufferOffset.value += pcmd.pointed.ElemCount * ImDrawIdxVar.size.toUInt()
        }
    }

    // Restore modified GL state
    glUseProgram(lastProgram.value.toUInt())
    glBindTexture(GL_TEXTURE_2D, lastTexture.value.toUInt())
//    #ifdef GL_SAMPLER_BINDING
    glBindSampler(0u, lastSampler.value.toUInt())
//    #endif
    glActiveTexture(lastActiveTexture.value.toUInt())
    glBindBuffer(GL_ARRAY_BUFFER, lastArrayBuffer.value.toUInt())
    glBlendEquationSeparate(lastBlendEquationRgb.value.toUInt(), lastBlendEquationAlpha.value.toUInt())
    glBlendFuncSeparate(lastBlendSrcRgb.value.toUInt(), lastBlendDstRgb.value.toUInt(), lastBlendSrcAlpha.value.toUInt(), lastBlendDstAlpha.value.toUInt())
    if (lastEnableBlend != GL_TRUE.toUByte()) glEnable(GL_BLEND) else glDisable(GL_BLEND)
    if (lastEnableCullFace != GL_TRUE.toUByte()) glEnable(GL_CULL_FACE) else glDisable(GL_CULL_FACE)
    if (lastEnableDepthTest != GL_TRUE.toUByte()) glEnable(GL_DEPTH_TEST) else glDisable(GL_DEPTH_TEST)
    if (lastEnableScissorTest != GL_TRUE.toUByte()) glEnable(GL_SCISSOR_TEST) else glDisable(GL_SCISSOR_TEST)
//    #ifdef GL_POLYGON_MODE
    glPolygonMode(GL_FRONT_AND_BACK, lastPolygonMode[0].toUInt())
//    #endif
    glViewport(lastViewport[0], lastViewport[1], lastViewport[2], lastViewport[3])
    glScissor(lastScissorBox[0], lastScissorBox[1], lastScissorBox[2], lastScissorBox[3])
}

private fun igOpenGL3CreateFontsTexture(): Boolean = memScoped {
    // Build texture atlas
    val io = igGetIO()
    val pixels = alloc<CPointerVar<UByteVar>>()
    val width = alloc<IntVar>()
    val height = alloc<IntVar>()
    ImFontAtlas_GetTexDataAsRGBA32(io!!.pointed.Fonts, pixels.ptr, width.ptr, height.ptr, null)   // Load as RGBA 32-bits (75% of the memory is wasted, but default font is so small) because it is more likely to be compatible with user's existing shaders. If your ImTextureId represent a higher-level concept than just a GL texture id, consider calling GetTexDataAsAlpha8() instead to save on GPU memory.

    // Upload texture to graphics system
    val lastTexture = alloc<GLintVar>()
    glGetIntegerv(GL_TEXTURE_BINDING_2D, lastTexture.ptr)
    glGenTextures(1, fontTexture.ptr)
    glBindTexture(GL_TEXTURE_2D, fontTexture.value)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR.toInt())
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR.toInt())
    if (GL_UNPACK_ROW_LENGTH != 0u) {
        glPixelStorei(GL_UNPACK_ROW_LENGTH, 0)
    }
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA.toInt(), width.value, height.value, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels.value)

    // Store our identifier
    io.pointed.Fonts!!.pointed.TexID = fontTexture.value.toLong().toCPointer()

    // Restore state
    glBindTexture(GL_TEXTURE_2D, lastTexture.value.toUInt())

    return@memScoped true
}

private fun igOpenGL3DestroyFontsTexture() {
    if (fontTexture.value != 0u) {
        val io = igGetIO()
        glDeleteTextures(1, fontTexture.ptr)
        io!!.pointed.Fonts!!.pointed.TexID = 0L.toCPointer()
        fontTexture.value = 0u
    }
}

private fun checkShader(handle: GLuint, desc: String): Boolean = memScoped {
    val status = alloc<IntVar>()
    val logLength = alloc<IntVar>()
    glGetShaderiv(handle, GL_COMPILE_STATUS, status.ptr)
    glGetShaderiv(handle, GL_INFO_LOG_LENGTH, logLength.ptr)
    if (status.value == GL_FALSE.toInt()) {
        Hazel.coreError("ERROR: igOpenGL3CreateDeviceObjects: failed to compile $desc!")
    }
    if (logLength.value > 0) {
        Hazel.coreError(glGetShaderInfoLog(handle))
    }
    return@memScoped status.value == GL_TRUE.toInt()
}

private fun checkProgram(handle: GLuint, desc: String): Boolean = memScoped {
    val status = alloc<IntVar>()
    val logLength = alloc<IntVar>()
    glGetProgramiv(handle, GL_LINK_STATUS, status.ptr)
    glGetProgramiv(handle, GL_INFO_LOG_LENGTH, logLength.ptr)
    if (status.value == GL_FALSE.toInt()) {
        Hazel.coreError("ERROR: igOpenGL3CreateDeviceObjects: failed to link $desc!")
    }
    if (logLength.value > 0) {
        Hazel.coreError(glGetProgramInfoLog(handle))
    }
    return@memScoped status.value == GL_TRUE.toInt()
}

private fun igOpenGL3CreateDeviceObjects(): Boolean = memScoped {
    // Backup GL state
    val lastTexture = alloc<GLintVar>()
    val lastArrayBuffer = alloc<GLintVar>()
    glGetIntegerv(GL_TEXTURE_BINDING_2D, lastTexture.ptr)
    glGetIntegerv(GL_ARRAY_BUFFER_BINDING, lastArrayBuffer.ptr)

    // Parse GLSL version string
    val glslVersion = alloc<IntVar> { value = 130 }
    sscanf(glslVersionString, "#version %d", glslVersion.ptr)

    val vertexShaderGlsl120 = """
        uniform mat4 ProjMtx;
        attribute vec2 Position;
        attribute vec2 UV;
        attribute vec4 Color;
        varying vec2 Frag_UV;
        varying vec4 Frag_Color;
        void main()
        {
            Frag_UV = UV;
            Frag_Color = Color;
            gl_Position = ProjMtx * vec4(Position.xy,0,1);
        }
        """.trimIndent()

    val vertexShaderGlsl130 = """
        uniform mat4 ProjMtx;
        in vec2 Position;
        in vec2 UV;
        in vec4 Color;
        out vec2 Frag_UV;
        out vec4 Frag_Color;
        void main()
        {
            Frag_UV = UV;
            Frag_Color = Color;
            gl_Position = ProjMtx * vec4(Position.xy,0,1);
        }
        """.trimIndent()

    val vertexShaderGlsl300Es = """
        precision mediump float;
        layout (location = 0) in vec2 Position;
        layout (location = 1) in vec2 UV;
        layout (location = 2) in vec4 Color;
        uniform mat4 ProjMtx;
        out vec2 Frag_UV;
        out vec4 Frag_Color;
        void main()
        {
            Frag_UV = UV;
            Frag_Color = Color;
            gl_Position = ProjMtx * vec4(Position.xy,0,1);
        }
        """.trimIndent()

    val vertexShaderGlsl410Core = """
        layout (location = 0) in vec2 Position;
        layout (location = 1) in vec2 UV;
        layout (location = 2) in vec4 Color;
        uniform mat4 ProjMtx;
        out vec2 Frag_UV;
        out vec4 Frag_Color;
        void main()
        {
        Frag_UV = UV;
        Frag_Color = Color;
        gl_Position = ProjMtx * vec4(Position.xy,0,1);
        }
        """.trimIndent()

    val fragmentShaderGlsl120 = """
        ifdef GL_ES
            precision mediump float;
        #endif
        uniform sampler2D Texture;
        varying vec2 Frag_UV;
        varying vec4 Frag_Color;
        void main()
        {
            gl_FragColor = Frag_Color * texture2D(Texture, Frag_UV.st);
        }
        """.trimIndent()

    val fragmentShaderGlsl130 = """
        uniform sampler2D Texture;
        in vec2 Frag_UV;
        in vec4 Frag_Color;
        out vec4 Out_Color;
        void main()
        {
            Out_Color = Frag_Color * texture(Texture, Frag_UV.st);
        }
        """.trimIndent()

    val fragmentShaderGlsl300Es = """
        precision mediump float;
        uniform sampler2D Texture;
        in vec2 Frag_UV;
        in vec4 Frag_Color;
        layout (location = 0) out vec4 Out_Color;
        void main()
        {
            Out_Color = Frag_Color * texture(Texture, Frag_UV.st);
        }
        """.trimIndent()

    val fragmentShaderGlsl410Core = """
        in vec2 Frag_UV;
        in vec4 Frag_Color;
        uniform sampler2D Texture;
        layout (location = 0) out vec4 Out_Color;
        void main()
        {
            Out_Color = Frag_Color * texture(Texture, Frag_UV.st);
        }
        """.trimIndent()

    // Select shaders matching our GLSL versions
    val (vertexShader, fragmentShader) = when {
        glslVersion.value < 130 -> vertexShaderGlsl120 to fragmentShaderGlsl120
        glslVersion.value >= 410 -> vertexShaderGlsl410Core to fragmentShaderGlsl410Core
        glslVersion.value == 300 -> vertexShaderGlsl300Es to fragmentShaderGlsl300Es
        else -> vertexShaderGlsl130 to fragmentShaderGlsl130
    }

    // Create shaders
    val vertexShaderWithVersion = "$glslVersionString\n$vertexShader"
    vertHandle = glCreateShader(GL_VERTEX_SHADER)
    glShaderSource(vertHandle, vertexShaderWithVersion)
    glCompileShader(vertHandle)
    checkShader(vertHandle, "vertex shader")

    val fragmentShaderWithVersion = arrayOf("$glslVersionString\n", fragmentShader).toCStringArray(scope)
    fragHandle = glCreateShader(GL_FRAGMENT_SHADER)
    glShaderSource(fragHandle, 2, fragmentShaderWithVersion, null)
    glCompileShader(fragHandle)
    checkShader(fragHandle, "fragment shader")

    shaderHandle = glCreateProgram()
    glAttachShader(shaderHandle, vertHandle)
    glAttachShader(shaderHandle, fragHandle)
    glLinkProgram(shaderHandle)
    checkProgram(shaderHandle, "shader program")

    attribLocationTex = glGetUniformLocation(shaderHandle, "Texture".cstr)
    attribLocationProjMtx = glGetUniformLocation(shaderHandle, "ProjMtx".cstr)
    attribLocationPosition = glGetAttribLocation(shaderHandle, "Position".cstr)
    attribLocationUV = glGetAttribLocation(shaderHandle, "UV".cstr)
    attribLocationColor = glGetAttribLocation(shaderHandle, "Color".cstr)

    // Create buffers
    glGenBuffers(1, vboHandle.ptr)
    glGenBuffers(1, elementsHandle.ptr)

    igOpenGL3CreateFontsTexture()

    // Restore modified GL state
    glBindTexture(GL_TEXTURE_2D, lastTexture.value.convert())
    glBindBuffer(GL_ARRAY_BUFFER, lastArrayBuffer.value.convert())

    return@memScoped true
}

private fun igOpenGL3DestroyDeviceObjects() {
    if (vboHandle.value != 0u) glDeleteBuffers(1, vboHandle.ptr)
    if (elementsHandle.value != 0u) glDeleteBuffers(1, elementsHandle.ptr)
    vboHandle.value = 0u
    elementsHandle.value = 0u

    if (shaderHandle != 0u && vertHandle != 0u) glDetachShader(shaderHandle, vertHandle)
    if (vertHandle != 0u) glDeleteShader(vertHandle)
    vertHandle = 0u

    if (shaderHandle != 0u && fragHandle != 0u) glDetachShader(shaderHandle, fragHandle)
    if (fragHandle != 0u) glDeleteShader(fragHandle)
    fragHandle = 0u

    if (shaderHandle != 0u) glDeleteProgram(shaderHandle)
    shaderHandle = 0u

    igOpenGL3DestroyFontsTexture()
}


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

        igOpenGL3Init("#version 410")
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

        igOpenGL3NewFrame()
        igNewFrame()

        igShowDemoWindow(show.ptr)

        igRender()
        igOpenGL3RenderDrawData(igGetDrawData()!!)
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
