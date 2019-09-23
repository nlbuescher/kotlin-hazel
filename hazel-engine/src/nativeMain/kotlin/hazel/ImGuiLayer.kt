package hazel

import cglfw.GLFW_KEY_A
import cglfw.GLFW_KEY_BACKSPACE
import cglfw.GLFW_KEY_C
import cglfw.GLFW_KEY_DELETE
import cglfw.GLFW_KEY_DOWN
import cglfw.GLFW_KEY_END
import cglfw.GLFW_KEY_ENTER
import cglfw.GLFW_KEY_ESCAPE
import cglfw.GLFW_KEY_HOME
import cglfw.GLFW_KEY_INSERT
import cglfw.GLFW_KEY_LEFT
import cglfw.GLFW_KEY_LEFT_ALT
import cglfw.GLFW_KEY_LEFT_CONTROL
import cglfw.GLFW_KEY_LEFT_SHIFT
import cglfw.GLFW_KEY_LEFT_SUPER
import cglfw.GLFW_KEY_PAGE_DOWN
import cglfw.GLFW_KEY_PAGE_UP
import cglfw.GLFW_KEY_RIGHT
import cglfw.GLFW_KEY_RIGHT_ALT
import cglfw.GLFW_KEY_RIGHT_CONTROL
import cglfw.GLFW_KEY_RIGHT_SHIFT
import cglfw.GLFW_KEY_RIGHT_SUPER
import cglfw.GLFW_KEY_SPACE
import cglfw.GLFW_KEY_TAB
import cglfw.GLFW_KEY_UP
import cglfw.GLFW_KEY_V
import cglfw.GLFW_KEY_X
import cglfw.GLFW_KEY_Y
import cglfw.GLFW_KEY_Z
import cglfw.glfwGetTime
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
import cimgui._igShowDemoWindow
import cimgui.igCreateContext
import cimgui.igGetDrawData
import cimgui.igGetIO
import cimgui.igNewFrame
import cimgui.igRender
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
import kotlinx.cinterop.CPointed
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CPointerVar
import kotlinx.cinterop.FloatVar
import kotlinx.cinterop.IntVar
import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.UIntVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
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

fun igOpenGL3Init(glslVersion: String? = null): Boolean {
    val io = igGetIO()
    io!!.pointed.BackendRendererName = "imgui_impl_opengl3".cstr.placeTo(scope)

    // Store GLSL version string so we can refer to it later in case we recreate shaders. Note: GLSL version is NOT the same as GL version. Leave this to NULL if unsure.
    glslVersionString = glslVersion ?: "#version 130"

    return true
}

fun igOpenGL3Shutdown() {
    igOpenGL3DestroyDeviceObjects()
    scope.clear()
}

fun igOpenGL3NewFrame() {
    if (fontTexture.value == 0u)
        igOpenGL3CreateDeviceObjects()
}

fun igOpenGL3RenderDrawData(draw_data: CPointer<ImDrawData>): Unit = memScoped {
    // Avoid rendering when minimized, scale coordinates for retina displays (screen coordinates != framebuffer coordinates)
    val fb_width = (draw_data.pointed.DisplaySize.x * draw_data.pointed.FramebufferScale.x).toInt()
    val fb_height = (draw_data.pointed.DisplaySize.y * draw_data.pointed.FramebufferScale.y).toInt()
    if (fb_width <= 0 || fb_height <= 0)
        return

    // Backup GL state
    val last_active_texture = alloc<GLenumVar>(); glGetIntegerv(GL_ACTIVE_TEXTURE, last_active_texture.ptr.reinterpret())
    glActiveTexture(GL_TEXTURE0)
    val last_program = alloc<GLintVar>(); glGetIntegerv(GL_CURRENT_PROGRAM, last_program.ptr)
    val last_texture = alloc<GLintVar>(); glGetIntegerv(GL_TEXTURE_BINDING_2D, last_texture.ptr)
//    #ifdef GL_SAMPLER_BINDING
    val last_sampler = alloc<GLintVar>(); glGetIntegerv(GL_SAMPLER_BINDING, last_sampler.ptr)
//    #endif
    val last_array_buffer = alloc<GLintVar>(); glGetIntegerv(GL_ARRAY_BUFFER_BINDING, last_array_buffer.ptr)
//    #ifdef GL_POLYGON_MODE
    val last_polygon_mode = allocArray<GLintVar>(2); glGetIntegerv(GL_POLYGON_MODE, last_polygon_mode)
//    #endif
    val last_viewport = allocArray<GLintVar>(4); glGetIntegerv(GL_VIEWPORT, last_viewport)
    val last_scissor_box = allocArray<GLintVar>(4); glGetIntegerv(GL_SCISSOR_BOX, last_scissor_box)
    val last_blend_src_rgb = alloc<GLenumVar>(); glGetIntegerv(GL_BLEND_SRC_RGB, last_blend_src_rgb.ptr.reinterpret())
    val last_blend_dst_rgb = alloc<GLenumVar>(); glGetIntegerv(GL_BLEND_DST_RGB, last_blend_dst_rgb.ptr.reinterpret())
    val last_blend_src_alpha = alloc<GLenumVar>(); glGetIntegerv(GL_BLEND_SRC_ALPHA, last_blend_src_alpha.ptr.reinterpret())
    val last_blend_dst_alpha = alloc<GLenumVar>(); glGetIntegerv(GL_BLEND_DST_ALPHA, last_blend_dst_alpha.ptr.reinterpret())
    val last_blend_equation_rgb = alloc<GLenumVar>(); glGetIntegerv(GL_BLEND_EQUATION_RGB, last_blend_equation_rgb.ptr.reinterpret())
    val last_blend_equation_alpha = alloc<GLenumVar>(); glGetIntegerv(GL_BLEND_EQUATION_ALPHA, last_blend_equation_alpha.ptr.reinterpret())
    val last_enable_blend = glIsEnabled(GL_BLEND)
    val last_enable_cull_face = glIsEnabled(GL_CULL_FACE)
    val last_enable_depth_test = glIsEnabled(GL_DEPTH_TEST)
    val last_enable_scissor_test = glIsEnabled(GL_SCISSOR_TEST)
    var clip_origin_lower_left = true
//    #if defined(GL_CLIP_ORIGIN) && !defined(__APPLE__)
    val last_clip_origin = alloc<GLenumVar> { value = 0u }; glGetIntegerv(GL_CLIP_ORIGIN, last_clip_origin.ptr.reinterpret()) // Support for GL 4.5's glClipControl(GL_UPPER_LEFT)
    if (last_clip_origin.value == GL_UPPER_LEFT)
        clip_origin_lower_left = false
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
    glViewport(0, 0, fb_width, fb_height)
    val L = draw_data.pointed.DisplayPos.x
    val R = draw_data.pointed.DisplayPos.x + draw_data.pointed.DisplaySize.x
    val T = draw_data.pointed.DisplayPos.y
    val B = draw_data.pointed.DisplayPos.y + draw_data.pointed.DisplaySize.y
    val ortho_projection = allocArray<FloatVar>(16)
    ortho_projection[0] = 2f / (R - L)
    ortho_projection[1] = 0f
    ortho_projection[2] = 0f
    ortho_projection[3] = 0f

    ortho_projection[4] = 0f
    ortho_projection[5] = 2f / (T - B)
    ortho_projection[6] = 0f
    ortho_projection[7] = 0f

    ortho_projection[8] = 0f
    ortho_projection[9] = 0f
    ortho_projection[10] = -1f
    ortho_projection[11] = 0f

    ortho_projection[12] = (R + L) / (L - R)
    ortho_projection[13] = (T + B) / (B - T)
    ortho_projection[14] = 0f
    ortho_projection[15] = 1f

    glUseProgram(shaderHandle)
    glUniform1i(attribLocationTex, 0)
    glUniformMatrix4fv(attribLocationProjMtx, 1, GL_FALSE.toUByte(), ortho_projection)
//    #ifdef GL_SAMPLER_BINDING
    glBindSampler(0u, 0u) // We use combined texture/sampler state. Applications using GL 3.3 may set that otherwise.
//    #endif

//    #ifndef IMGUI_IMPL_OPENGL_ES2
    // Recreate the VAO every time
    // (This is to easily allow multiple GL contexts. VAO are not shared among GL contexts, and we don't track creation/deletion of windows so we don't have an obvious key to use to cache them.)
    val vao_handle = alloc<GLuintVar>()
    glGenVertexArrays(1, vao_handle.ptr)
    glBindVertexArray(vao_handle.value)
//    #endif
    glBindBuffer(GL_ARRAY_BUFFER, vboHandle.value)
    glEnableVertexAttribArray(attribLocationPosition.toUInt())
    glEnableVertexAttribArray(attribLocationUV.toUInt())
    glEnableVertexAttribArray(attribLocationColor.toUInt())
    glVertexAttribPointer(attribLocationPosition.toUInt(), 2, GL_FLOAT, GL_FALSE.toUByte(), ImDrawVert.size.toInt(), ImDrawVert_pos_offset())
    glVertexAttribPointer(attribLocationUV.toUInt(), 2, GL_FLOAT, GL_FALSE.toUByte(), ImDrawVert.size.toInt(), ImDrawVert_uv_offset())
    glVertexAttribPointer(attribLocationColor.toUInt(), 4, GL_UNSIGNED_BYTE, GL_TRUE.toUByte(), ImDrawVert.size.toInt(), ImDrawVert_col_offset())

    // Will project scissor/clipping rectangles into framebuffer space
    val clip_off = draw_data.pointed.DisplayPos         // (0,0) unless using multi-viewports
    val clip_scale = draw_data.pointed.FramebufferScale // (1,1) unless using retina display which are often (2,2)

    // Render command lists
    for (n in 0 until draw_data.pointed.CmdListsCount) {
        val cmd_list = draw_data.pointed.CmdLists!![n]
        val idx_buffer_offset = alloc<size_tVar> { value = 0u }

        glBindBuffer(GL_ARRAY_BUFFER, vboHandle.value)
        glBufferData(GL_ARRAY_BUFFER, cmd_list!!.pointed.VtxBuffer.Size * ImDrawVert.size, cmd_list.pointed.VtxBuffer.Data, GL_STREAM_DRAW)

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementsHandle.value)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, cmd_list.pointed.IdxBuffer.Size * ImDrawIdxVar.size, cmd_list.pointed.IdxBuffer.Data, GL_STREAM_DRAW)

        for (cmd_i in 0 until cmd_list.pointed.CmdBuffer.Size) {
            val pcmd = cmd_list.pointed.CmdBuffer.Data!![cmd_i].ptr
            if (pcmd.pointed.UserCallback != null) {
                // User callback (registered via ImDrawList::AddCallback)
                pcmd.pointed.UserCallback!!(cmd_list, pcmd)

            } else {
                // Project scissor/clipping rectangles into framebuffer space
                val clip_rect = alloc<ImVec4>()
                clip_rect.x = (pcmd.pointed.ClipRect.x - clip_off.x) * clip_scale.x
                clip_rect.y = (pcmd.pointed.ClipRect.y - clip_off.y) * clip_scale.y
                clip_rect.z = (pcmd.pointed.ClipRect.z - clip_off.x) * clip_scale.x
                clip_rect.w = (pcmd.pointed.ClipRect.w - clip_off.y) * clip_scale.y

                if (clip_rect.x < fb_width && clip_rect.y < fb_height && clip_rect.z >= 0.0f && clip_rect.w >= 0.0f) {
                    // Apply scissor/clipping rectangle
                    if (clip_origin_lower_left)
                        glScissor(clip_rect.x.toInt(), (fb_height - clip_rect.w).toInt(), (clip_rect.z - clip_rect.x).toInt(), (clip_rect.w - clip_rect.y).toInt())
                    else
                        glScissor(clip_rect.x.toInt(), clip_rect.y.toInt(), clip_rect.z.toInt(), clip_rect.w.toInt()) // Support for GL 4.5's glClipControl(GL_UPPER_LEFT)

                    // Bind texture, Draw
                    glBindTexture(GL_TEXTURE_2D, pcmd.pointed.TextureId.toLong().toUInt())
                    glDrawElements(GL_TRIANGLES, pcmd.pointed.ElemCount.toInt(), GL_UNSIGNED_SHORT, idx_buffer_offset.value.toLong().toCPointer<CPointed>())
                }
            }
            idx_buffer_offset.value += pcmd.pointed.ElemCount * ImDrawIdxVar.size.toUInt()
        }
    }

    // Restore modified GL state
    glUseProgram(last_program.value.toUInt())
    glBindTexture(GL_TEXTURE_2D, last_texture.value.toUInt())
//    #ifdef GL_SAMPLER_BINDING
    glBindSampler(0u, last_sampler.value.toUInt())
//    #endif
    glActiveTexture(last_active_texture.value.toUInt())
    glBindBuffer(GL_ARRAY_BUFFER, last_array_buffer.value.toUInt())
    glBlendEquationSeparate(last_blend_equation_rgb.value.toUInt(), last_blend_equation_alpha.value.toUInt())
    glBlendFuncSeparate(last_blend_src_rgb.value.toUInt(), last_blend_dst_rgb.value.toUInt(), last_blend_src_alpha.value.toUInt(), last_blend_dst_alpha.value.toUInt())
    if (last_enable_blend != GL_TRUE.toUByte()) glEnable(GL_BLEND) else glDisable(GL_BLEND)
    if (last_enable_cull_face != GL_TRUE.toUByte()) glEnable(GL_CULL_FACE) else glDisable(GL_CULL_FACE)
    if (last_enable_depth_test != GL_TRUE.toUByte()) glEnable(GL_DEPTH_TEST) else glDisable(GL_DEPTH_TEST)
    if (last_enable_scissor_test != GL_TRUE.toUByte()) glEnable(GL_SCISSOR_TEST) else glDisable(GL_SCISSOR_TEST)
//    #ifdef GL_POLYGON_MODE
    glPolygonMode(GL_FRONT_AND_BACK, last_polygon_mode[0].toUInt())
//    #endif
    glViewport(last_viewport[0], last_viewport[1], last_viewport[2], last_viewport[3])
    glScissor(last_scissor_box[0], last_scissor_box[1], last_scissor_box[2], last_scissor_box[3])
}

fun igOpenGL3CreateFontsTexture(): Boolean = memScoped {
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

fun igOpenGL3DestroyFontsTexture() {
    if (fontTexture.value != 0u) {
        val io = igGetIO()
        glDeleteTextures(1, fontTexture.ptr)
        io!!.pointed.Fonts!!.pointed.TexID = 0L.toCPointer()
        fontTexture.value = 0u
    }
}

fun checkShader(handle: GLuint, desc: String): Boolean = memScoped {
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

fun checkProgram(handle: GLuint, desc: String): Boolean = memScoped {
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

fun igOpenGL3CreateDeviceObjects(): Boolean = memScoped {
    // Backup GL state
    val lastTexture = alloc<GLintVar>()
    val lastArrayBuffer = alloc<GLintVar>()
    glGetIntegerv(GL_TEXTURE_BINDING_2D, lastTexture.ptr)
    glGetIntegerv(GL_ARRAY_BUFFER_BINDING, lastArrayBuffer.ptr)

    // Parse GLSL version string
    val glsl_version = alloc<IntVar> { value = 130 }
    sscanf(glslVersionString, "#version %d", glsl_version.ptr)

    val vertex_shader_glsl_120 =
        "uniform mat4 ProjMtx;\n" +
            "attribute vec2 Position;\n" +
            "attribute vec2 UV;\n" +
            "attribute vec4 Color;\n" +
            "varying vec2 Frag_UV;\n" +
            "varying vec4 Frag_Color;\n" +
            "void main()\n" +
            "{\n" +
            "    Frag_UV = UV;\n" +
            "    Frag_Color = Color;\n" +
            "    gl_Position = ProjMtx * vec4(Position.xy,0,1);\n" +
            "}\n"

    val vertex_shader_glsl_130 =
        "uniform mat4 ProjMtx;\n" +
            "in vec2 Position;\n" +
            "in vec2 UV;\n" +
            "in vec4 Color;\n" +
            "out vec2 Frag_UV;\n" +
            "out vec4 Frag_Color;\n" +
            "void main()\n" +
            "{\n" +
            "    Frag_UV = UV;\n" +
            "    Frag_Color = Color;\n" +
            "    gl_Position = ProjMtx * vec4(Position.xy,0,1);\n" +
            "}\n"

    val vertex_shader_glsl_300_es =
        "precision mediump float;\n" +
            "layout (location = 0) in vec2 Position;\n" +
            "layout (location = 1) in vec2 UV;\n" +
            "layout (location = 2) in vec4 Color;\n" +
            "uniform mat4 ProjMtx;\n" +
            "out vec2 Frag_UV;\n" +
            "out vec4 Frag_Color;\n" +
            "void main()\n" +
            "{\n" +
            "    Frag_UV = UV;\n" +
            "    Frag_Color = Color;\n" +
            "    gl_Position = ProjMtx * vec4(Position.xy,0,1);\n" +
            "}\n"

    val vertex_shader_glsl_410_core =
        "layout (location = 0) in vec2 Position;\n" +
            "layout (location = 1) in vec2 UV;\n" +
            "layout (location = 2) in vec4 Color;\n" +
            "uniform mat4 ProjMtx;\n" +
            "out vec2 Frag_UV;\n" +
            "out vec4 Frag_Color;\n" +
            "void main()\n" +
            "{\n" +
            "    Frag_UV = UV;\n" +
            "    Frag_Color = Color;\n" +
            "    gl_Position = ProjMtx * vec4(Position.xy,0,1);\n" +
            "}\n"

    val fragment_shader_glsl_120 =
        "#ifdef GL_ES\n" +
            "    precision mediump float;\n" +
            "#endif\n" +
            "uniform sampler2D Texture;\n" +
            "varying vec2 Frag_UV;\n" +
            "varying vec4 Frag_Color;\n" +
            "void main()\n" +
            "{\n" +
            "    gl_FragColor = Frag_Color * texture2D(Texture, Frag_UV.st);\n" +
            "}\n"

    val fragment_shader_glsl_130 =
        "uniform sampler2D Texture;\n" +
            "in vec2 Frag_UV;\n" +
            "in vec4 Frag_Color;\n" +
            "out vec4 Out_Color;\n" +
            "void main()\n" +
            "{\n" +
            "    Out_Color = Frag_Color * texture(Texture, Frag_UV.st);\n" +
            "}\n"

    val fragment_shader_glsl_300_es =
        "precision mediump float;\n" +
            "uniform sampler2D Texture;\n" +
            "in vec2 Frag_UV;\n" +
            "in vec4 Frag_Color;\n" +
            "layout (location = 0) out vec4 Out_Color;\n" +
            "void main()\n" +
            "{\n" +
            "    Out_Color = Frag_Color * texture(Texture, Frag_UV.st);\n" +
            "}\n"

    val fragment_shader_glsl_410_core =
        "in vec2 Frag_UV;\n" +
            "in vec4 Frag_Color;\n" +
            "uniform sampler2D Texture;\n" +
            "layout (location = 0) out vec4 Out_Color;\n" +
            "void main()\n" +
            "{\n" +
            "    Out_Color = Frag_Color * texture(Texture, Frag_UV.st);\n" +
            "}\n"

    // Select shaders matching our GLSL versions
    val vertex_shader: String
    val fragment_shader: String
    if (glsl_version.value < 130) {
        vertex_shader = vertex_shader_glsl_120
        fragment_shader = fragment_shader_glsl_120
    } else if (glsl_version.value >= 410) {
        vertex_shader = vertex_shader_glsl_410_core
        fragment_shader = fragment_shader_glsl_410_core
    } else if (glsl_version.value == 300) {
        vertex_shader = vertex_shader_glsl_300_es
        fragment_shader = fragment_shader_glsl_300_es
    } else {
        vertex_shader = vertex_shader_glsl_130
        fragment_shader = fragment_shader_glsl_130
    }

    // Create shaders
    val vertex_shader_with_version = "$glslVersionString\n$vertex_shader"
    vertHandle = glCreateShader(GL_VERTEX_SHADER)
    glShaderSource(vertHandle, vertex_shader_with_version)
    glCompileShader(vertHandle)
    checkShader(vertHandle, "vertex shader")

    val fragment_shader_with_version = arrayOf("$glslVersionString\n", fragment_shader).toCStringArray(scope)
    fragHandle = glCreateShader(GL_FRAGMENT_SHADER)
    glShaderSource(fragHandle, 2, fragment_shader_with_version, null)
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
    glBindTexture(GL_TEXTURE_2D, lastTexture.value.toUInt())
    glBindBuffer(GL_ARRAY_BUFFER, lastArrayBuffer.value.toUInt())

    return@memScoped true
}

fun igOpenGL3DestroyDeviceObjects() {
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

            // TEMPORARY: should eventually use Hazel key codes
            KeyMap[ImGuiKey_Tab.ordinal] = GLFW_KEY_TAB
            KeyMap[ImGuiKey_LeftArrow.ordinal] = GLFW_KEY_LEFT
            KeyMap[ImGuiKey_RightArrow.ordinal] = GLFW_KEY_RIGHT
            KeyMap[ImGuiKey_UpArrow.ordinal] = GLFW_KEY_UP
            KeyMap[ImGuiKey_DownArrow.ordinal] = GLFW_KEY_DOWN
            KeyMap[ImGuiKey_PageUp.ordinal] = GLFW_KEY_PAGE_UP
            KeyMap[ImGuiKey_PageDown.ordinal] = GLFW_KEY_PAGE_DOWN
            KeyMap[ImGuiKey_Home.ordinal] = GLFW_KEY_HOME
            KeyMap[ImGuiKey_End.ordinal] = GLFW_KEY_END
            KeyMap[ImGuiKey_Insert.ordinal] = GLFW_KEY_INSERT
            KeyMap[ImGuiKey_Delete.ordinal] = GLFW_KEY_DELETE
            KeyMap[ImGuiKey_Backspace.ordinal] = GLFW_KEY_BACKSPACE
            KeyMap[ImGuiKey_Space.ordinal] = GLFW_KEY_SPACE
            KeyMap[ImGuiKey_Enter.ordinal] = GLFW_KEY_ENTER
            KeyMap[ImGuiKey_Escape.ordinal] = GLFW_KEY_ESCAPE
            KeyMap[ImGuiKey_A.ordinal] = GLFW_KEY_A
            KeyMap[ImGuiKey_C.ordinal] = GLFW_KEY_C
            KeyMap[ImGuiKey_V.ordinal] = GLFW_KEY_V
            KeyMap[ImGuiKey_X.ordinal] = GLFW_KEY_X
            KeyMap[ImGuiKey_Y.ordinal] = GLFW_KEY_Y
            KeyMap[ImGuiKey_Z.ordinal] = GLFW_KEY_Z
        }

        igOpenGL3Init("#version 410")
    }

    private val show: IntVar = nativeHeap.alloc()

    override fun onUpdate() {
        val io = igGetIO()!!.pointed
        val window = Hazel.application.window
        io.DisplaySize.x = window.size.first.toFloat()
        io.DisplaySize.y = window.size.second.toFloat()

        val time = glfwGetTime().toFloat()
        io.DeltaTime = if (lastTime > 0f) time - lastTime else 1f / 60f
        lastTime = time

        igOpenGL3NewFrame()
        igNewFrame()

        _igShowDemoWindow(show.ptr)

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
        io.MouseDown[event.button].value = true

        return false
    }

    private fun onMouseButtonReleased(event: MouseButtonReleasedEvent): Boolean {
        val io = igGetIO()!!.pointed
        io.MouseDown[event.button].value = false

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
        io.KeysDown[event.keyCode].value = true

        io.KeyCtrl = io.KeysDown[GLFW_KEY_LEFT_CONTROL].value || io.KeysDown[GLFW_KEY_RIGHT_CONTROL].value
        io.KeyShift = io.KeysDown[GLFW_KEY_LEFT_SHIFT].value || io.KeysDown[GLFW_KEY_RIGHT_SHIFT].value
        io.KeyAlt = io.KeysDown[GLFW_KEY_LEFT_ALT].value || io.KeysDown[GLFW_KEY_RIGHT_ALT].value
        io.KeySuper = io.KeysDown[GLFW_KEY_LEFT_SUPER].value || io.KeysDown[GLFW_KEY_RIGHT_SUPER].value

        return false
    }

    private fun onKeyReleased(event: KeyReleasedEvent): Boolean {
        val io = igGetIO()!!.pointed
        io.KeysDown[event.keyCode].value = false

        io.KeyCtrl = io.KeysDown[GLFW_KEY_LEFT_CONTROL].value || io.KeysDown[GLFW_KEY_RIGHT_CONTROL].value
        io.KeyShift = io.KeysDown[GLFW_KEY_LEFT_SHIFT].value || io.KeysDown[GLFW_KEY_RIGHT_SHIFT].value
        io.KeyAlt = io.KeysDown[GLFW_KEY_LEFT_ALT].value || io.KeysDown[GLFW_KEY_RIGHT_ALT].value
        io.KeySuper = io.KeysDown[GLFW_KEY_LEFT_SUPER].value || io.KeysDown[GLFW_KEY_RIGHT_SUPER].value

        return false
    }

    private fun onKeyTyped(event: KeyTypedEvent): Boolean {
        val io = igGetIO()
        if (event.keyCode in 1..0xFFFF) {
            ImGuiIO_AddInputCharacter(io, event.keyCode.toUInt())
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
