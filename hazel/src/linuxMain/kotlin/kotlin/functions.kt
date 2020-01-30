@file:Suppress("NOTHING_TO_INLINE")

package kotlin

import platform.linux.__NR_gettid
import platform.posix.raise
import platform.posix.SIGTRAP
import platform.posix.syscall

actual inline fun breakpoint() = raise(SIGTRAP)

actual inline fun getThreadId() = syscall(__NR_gettid)
