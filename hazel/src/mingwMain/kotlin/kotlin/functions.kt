@file:Suppress("NOTHING_TO_INLINE")

package kotlin

import platform.posix.__debugbreak
import platform.windows.GetCurrentThreadId

actual inline fun breakpoint() {
    __debugbreak()
}

actual inline fun getThreadId(): UInt {
    GetCurrentThreadId()
}
