package hazel.system

import io.ktor.utils.io.charsets.*
import io.ktor.utils.io.core.*
import kotlinx.cinterop.*
import platform.posix.*

class File(val path: String)

fun File.readBytes(): ByteArray {
	return fopen(path, "rb")?.let { file ->
		fseek(file, 0, SEEK_END)
		val size = ftell(file).toInt()
		fseek(file, 0, SEEK_SET)
		ByteArray(size).apply {
			fread(refTo(0), 1.convert(), size.convert(), file)
			fclose(file)
		}
	} ?: error("${strerror(errno)?.toKString()}")
}

fun File.writeBytes(array: ByteArray) {
	fopen(path, "wb")?.let { file ->
		fwrite(array.refTo(0), 1.convert(), array.size.convert(), file)
		fclose(file)
	} ?: error("${strerror(errno)?.toKString()}")
}

fun File.appendBytes(array: ByteArray) {
	fopen(path, "ab")?.let { file ->
		fwrite(array.refTo(0), 1.convert(), array.size.convert(), file)
		fclose(file)
	} ?: error("${strerror(errno)?.toKString()}")
}

fun File.readText(): String = readBytes().toKString()

fun File.writeText(text: String, charset: Charset = Charsets.UTF_8) = writeBytes(text.toByteArray(charset))

fun File.appendText(text: String, charset: Charset = Charsets.UTF_8) = appendBytes(text.toByteArray(charset))
