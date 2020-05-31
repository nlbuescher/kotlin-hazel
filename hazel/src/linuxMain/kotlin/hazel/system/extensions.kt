@file:Suppress("NOTHING_TO_INLINE")

package hazel.system

import platform.linux.SYS_gettid
import platform.posix.SIGTRAP
import platform.posix.raise
import platform.posix.syscall

actual inline fun breakpoint() {
	raise(SIGTRAP)
}

actual inline fun getThreadId(): ULong = syscall(SYS_gettid.toLong()).toULong()
