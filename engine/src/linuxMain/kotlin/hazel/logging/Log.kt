package hazel.logging

import kotlinx.cinterop.*
import platform.posix.localtime
import platform.posix.strftime
import platform.posix.time
import platform.posix.time_tVar


@ExperimentalUnsignedTypes
internal actual val timestamp: String
    get() = memScoped {
        val t = alloc<time_tVar>()
        val str = allocArray<ByteVar>(10)
        time(t.ptr)
        strftime(str, 10u, "%H:%M:%S", localtime(t.ptr))
        str.toKString()
    }
