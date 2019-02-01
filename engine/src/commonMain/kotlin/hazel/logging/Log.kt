package hazel.logging


internal lateinit var coreLogger: Logger
internal lateinit var clientLogger: Logger


internal expect val timestamp: String


internal class Logger(private val name: String) {
    private fun format(message: String) = "[$timestamp] $name: $message"

    fun trace(message: String) = println("$white${format(message)}$reset")
    fun debug(message: String) = println("$cyan${format(message)}$reset")
    fun info(message: String) = println("$green${format(message)}$reset")
    fun warn(message: String) = println("$yellow$bold${format(message)}$reset")
    fun error(message: String) = println("$red$bold${format(message)}$reset")
    fun critical(message: String) = println("$bold$onRed${format(message)}$reset")
}


// formatting codes
internal const val reset = "\u001B[m"
internal const val bold = "\u001B[1m"
internal const val dark = "\u001B[2m"
internal const val underline = "\u001B[3m"
internal const val blink = "\u001B[4m"
internal const val reverse = "\u001B[5m"
internal const val concealed = "\u001B[6m"
internal const val clearLine = "\u001B[7m"

// foreground colors
internal const val black = "\u001B[30m"
internal const val red = "\u001B[31m"
internal const val green = "\u001B[32m"
internal const val yellow = "\u001B[33m"
internal const val blue = "\u001B[34m"
internal const val magenta = "\u001B[35m"
internal const val cyan = "\u001B[36m"
internal const val white = "\u001B[37m"

// background colors
internal const val onBlack = "\u001B[40m"
internal const val onRed = "\u001B[41m"
internal const val onGreen = "\u001B[42m"
internal const val onYellow = "\u001B[43m"
internal const val onBlue = "\u001B[44m"
internal const val onMagenta = "\u001B[45m"
internal const val onCyan = "\u001B[46m"
internal const val onWhite = "\u001B[47m"


