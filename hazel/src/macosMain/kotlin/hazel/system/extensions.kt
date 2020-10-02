@file:Suppress("NOTHING_TO_INLINE")

package hazel.system

import kotlinx.cinterop.*
import platform.posix.*

actual inline fun breakpoint() {
	raise(SIGTRAP)
}

actual inline fun getThreadId(): ULong = memScoped {
	val ret = alloc<ULongVar>()
	pthread_threadid_np(null, ret.ptr)
	return ret.value
}
