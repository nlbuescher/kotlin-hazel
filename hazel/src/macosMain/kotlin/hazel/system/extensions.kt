@file:Suppress("NOTHING_TO_INLINE")

package hazel.system

import hazel.cinterop.pthread_threadid
import kotlinx.cinterop.*
import platform.posix.SIGTRAP
import platform.posix.raise

actual inline fun breakpoint() {
	raise(SIGTRAP)
}

actual inline fun getThreadId(): ULong = memScoped {
	val ret = alloc<ULongVar>()
	pthread_threadid(null, ret.ptr)
	return ret.value
}
