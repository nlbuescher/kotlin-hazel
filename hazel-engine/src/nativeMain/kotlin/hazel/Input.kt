package hazel

expect object Input {
    fun isKeyPressed(keycode: Int): Boolean

    fun isMouseButtonPressed(button: Int): Boolean
    val mousePosition: Pair<Float, Float>
}
