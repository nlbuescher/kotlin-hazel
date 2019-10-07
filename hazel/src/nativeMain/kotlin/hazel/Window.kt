package hazel

import kotlinx.io.core.Closeable
import kotlin.native.concurrent.ThreadLocal


expect class Window : Closeable {

    var position: Pair<Int, Int>
    var size: Pair<Int, Int>

    fun swapBuffers()

    fun setEventCallback(callback: (Event) -> Unit)

    fun onUpdate()

    @ThreadLocal
    companion object {
        operator fun invoke(width: Int = 1280, height: Int = 720, title: String = "Hazel Engine"): Window
    }
}
