package kotlin

import platform.posix.__debugbreak
import platform.windows.GetCurrentThreadId

actual fun breakpoint() = __debugbreak()

actual fun getThreadId() = GetCurrentThreadId()
