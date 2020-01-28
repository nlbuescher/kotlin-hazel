package hazel.core

import com.kgl.glfw.Action
import com.kgl.glfw.KeyboardKey

object Input {
    fun isKeyPressed(key: Key): Boolean {
        val state = Hazel.application.window.internal.getKey(key.toGlfw())
        return state == Action.Press || state == Action.Repeat
    }

    fun isMouseButtonPressed(button: MouseButton): Boolean {
        val state = Hazel.application.window.internal.getMouseButton(button.toGlfw())
        return state == Action.Press
    }

    val mousePosition: Pair<Double, Double>
        get() = Hazel.application.window.internal.cursorPosition
}

fun Key.toGlfw() = KeyboardKey.from(value)
fun Key.Companion.fromGlfw(key: KeyboardKey) = Key(key.value)

fun MouseButton.toGlfw() = com.kgl.glfw.MouseButton.from(value)
fun MouseButton.Companion.fromGlfw(button: com.kgl.glfw.MouseButton) = MouseButton(button.value)

inline class Key(val value: Int) {
    companion object {
        // The unknown key
        val UNKNOWN = Key(-1)

        // Printable keys
        val SPACE = Key(32)
        val APOSTROPHE = Key(39)  // '
        val COMMA = Key(44)  // ,
        val MINUS = Key(45)  // -
        val PERIOD = Key(46)  // .
        val SLASH = Key(47)  // /
        val NUM_0 = Key(48)
        val NUM_1 = Key(49)
        val NUM_2 = Key(50)
        val NUM_3 = Key(51)
        val NUM_4 = Key(52)
        val NUM_5 = Key(53)
        val NUM_6 = Key(54)
        val NUM_7 = Key(55)
        val NUM_8 = Key(56)
        val NUM_9 = Key(57)
        val SEMICOLON = Key(59)  // ;
        val EQUAL = Key(61)  // =
        val A = Key(65)
        val B = Key(66)
        val C = Key(67)
        val D = Key(68)
        val E = Key(69)
        val F = Key(70)
        val G = Key(71)
        val H = Key(72)
        val I = Key(73)
        val J = Key(74)
        val K = Key(75)
        val L = Key(76)
        val M = Key(77)
        val N = Key(78)
        val O = Key(79)
        val P = Key(80)
        val Q = Key(81)
        val R = Key(82)
        val S = Key(83)
        val T = Key(84)
        val U = Key(85)
        val V = Key(86)
        val W = Key(87)
        val X = Key(88)
        val Y = Key(89)
        val Z = Key(90)
        val LEFT_BRACKET = Key(91)  // [
        val BACKSLASH = Key(92)  // \
        val RIGHT_BRACKET = Key(93)  // ]
        val GRAVE_ACCENT = Key(96)  // `
        val WORLD_1 = Key(161) // non-US #1
        val WORLD_2 = Key(162) // non-US #2

        // Function keys
        val ESCAPE = Key(256)
        val ENTER = Key(257)
        val TAB = Key(258)
        val BACKSPACE = Key(259)
        val INSERT = Key(260)
        val DELETE = Key(261)
        val RIGHT = Key(262)
        val LEFT = Key(263)
        val DOWN = Key(264)
        val UP = Key(265)
        val PAGE_UP = Key(266)
        val PAGE_DOWN = Key(267)
        val HOME = Key(268)
        val END = Key(269)
        val CAPS_LOCK = Key(280)
        val SCROLL_LOCK = Key(281)
        val NUM_LOCK = Key(282)
        val PRINT_SCREEN = Key(283)
        val PAUSE = Key(284)
        val F1 = Key(290)
        val F2 = Key(291)
        val F3 = Key(292)
        val F4 = Key(293)
        val F5 = Key(294)
        val F6 = Key(295)
        val F7 = Key(296)
        val F8 = Key(297)
        val F9 = Key(298)
        val F10 = Key(299)
        val F11 = Key(300)
        val F12 = Key(301)
        val F13 = Key(302)
        val F14 = Key(303)
        val F15 = Key(304)
        val F16 = Key(305)
        val F17 = Key(306)
        val F18 = Key(307)
        val F19 = Key(308)
        val F20 = Key(309)
        val F21 = Key(310)
        val F22 = Key(311)
        val F23 = Key(312)
        val F24 = Key(313)
        val F25 = Key(314)
        val KP_0 = Key(320)
        val KP_1 = Key(321)
        val KP_2 = Key(322)
        val KP_3 = Key(323)
        val KP_4 = Key(324)
        val KP_5 = Key(325)
        val KP_6 = Key(326)
        val KP_7 = Key(327)
        val KP_8 = Key(328)
        val KP_9 = Key(329)
        val KP_DECIMAL = Key(330)
        val KP_DIVIDE = Key(331)
        val KP_MULTIPLY = Key(332)
        val KP_SUBTRACT = Key(333)
        val KP_ADD = Key(334)
        val KP_ENTER = Key(335)
        val KP_EQUAL = Key(336)
        val LEFT_SHIFT = Key(340)
        val LEFT_CONTROL = Key(341)
        val LEFT_ALT = Key(342)
        val LEFT_SUPER = Key(343)
        val RIGHT_SHIFT = Key(344)
        val RIGHT_CONTROL = Key(345)
        val RIGHT_ALT = Key(346)
        val RIGHT_SUPER = Key(347)
        val MENU = Key(348)
    }

    override fun toString() = "$value"
}

inline class MouseButton(val value: Int) {
    companion object {
        val BUTTON_1 = MouseButton(0)
        val BUTTON_2 = MouseButton(1)
        val BUTTON_3 = MouseButton(2)
        val BUTTON_4 = MouseButton(3)
        val BUTTON_5 = MouseButton(4)
        val BUTTON_6 = MouseButton(5)
        val BUTTON_7 = MouseButton(6)
        val BUTTON_8 = MouseButton(7)
        val BUTTON_LEFT = BUTTON_1
        val BUTTON_RIGHT = BUTTON_2
        val BUTTON_MIDDLE = BUTTON_3
    }

    override fun toString() = "$value"
}
