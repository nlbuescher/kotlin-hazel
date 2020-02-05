@file:Suppress("NOTHING_TO_INLINE")

package hazel.system

import platform.linux.__NR_gettid
import platform.posix.SIGTRAP
import platform.posix.raise
import platform.posix.syscall

actual inline fun breakpoint() {
	raise(SIGTRAP)
}

actual inline fun getThreadId(): UInt {
	return syscall(__NR_gettid).toUInt()
}
