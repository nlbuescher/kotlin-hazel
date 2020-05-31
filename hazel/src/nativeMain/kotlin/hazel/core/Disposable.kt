package hazel.core

interface Disposable {
	fun dispose()
}

inline fun <T : Disposable, R> T.use(block: (T) -> R): R = try {
	block(this)
} finally {
	dispose()
}
