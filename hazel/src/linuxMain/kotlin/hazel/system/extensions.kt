@file:Suppress("NOTHING_TO_INLINE")

package hazel.system

import platform.linux.*
import platform.posix.*

actual inline fun breakpoint() {
	raise(SIGTRAP)
}

actual inline fun getThreadId(): ULong = syscall(SYS_gettid.toLong()).toULong()
