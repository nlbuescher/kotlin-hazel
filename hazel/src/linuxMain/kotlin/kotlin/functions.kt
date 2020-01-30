package kotlin

import platform.linux.__NR_gettid
import platform.posix.raise
import platform.posix.SIGTRAP
import platform.posix.syscall

actual fun breakpoint() = raise(SIGTRAP)

actual fun getThreadId() = syscall(__NR_gettid)
