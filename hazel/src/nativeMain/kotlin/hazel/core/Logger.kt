package hazel.core

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toKString
import platform.posix.localtime
import platform.posix.strftime
import platform.posix.time
import platform.posix.time_tVar

internal class Logger(private val name: String) {
	private val timestamp: String
		get() = memScoped {
			val t = alloc<time_tVar>()
			val str = allocArray<ByteVar>(10)
			time(t.ptr)
			strftime(str, 10, "%H:%M:%S", localtime(t.ptr))
			str.toKString()
		}

	private fun format(message: Any?) = "[$timestamp] $name: $message"

	fun trace(message: Any?) = println("$white${format(message)}$reset")
	fun debug(message: Any?) = println("$cyan${format(message)}$reset")
	fun info(message: Any?) = println("$green${format(message)}$reset")
	fun warn(message: Any?) = println("$yellow$bold${format(message)}$reset")
	fun error(message: Any?) = println("$red$bold${format(message)}$reset")
	fun critical(message: Any?) = println("$bold$onRed${format(message)}$reset")


	companion object {
		// formatting codes
		private const val reset = "\u001B[m"
		private const val bold = "\u001B[1m"
		private const val dark = "\u001B[2m"
		private const val underline = "\u001B[3m"
		private const val blink = "\u001B[4m"
		private const val reverse = "\u001B[5m"
		private const val concealed = "\u001B[6m"
		private const val clearLine = "\u001B[7m"

		// foreground colors
		private const val black = "\u001B[30m"
		private const val red = "\u001B[31m"
		private const val green = "\u001B[32m"
		private const val yellow = "\u001B[33m"
		private const val blue = "\u001B[34m"
		private const val magenta = "\u001B[35m"
		private const val cyan = "\u001B[36m"
		private const val white = "\u001B[37m"

		// background colors
		private const val onBlack = "\u001B[40m"
		private const val onRed = "\u001B[41m"
		private const val onGreen = "\u001B[42m"
		private const val onYellow = "\u001B[43m"
		private const val onBlue = "\u001B[44m"
		private const val onMagenta = "\u001B[45m"
		private const val onCyan = "\u001B[46m"
		private const val onWhite = "\u001B[47m"
	}
}
