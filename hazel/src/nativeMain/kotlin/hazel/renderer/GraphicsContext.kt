package hazel.renderer

import kotlinx.cinterop.CPointer
import cnames.structs.GLFWwindow

interface GraphicsContext {
    fun init()
    fun swapBuffers()
}
