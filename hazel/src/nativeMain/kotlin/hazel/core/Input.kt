package hazel.core

import com.kgl.glfw.Action
import com.kgl.glfw.KeyboardKey as GlfwKey
import com.kgl.glfw.MouseButton as GlfwMouseButton

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
        val _0 = Key(48)
        val _1 = Key(49)
        val _2 = Key(50)
        val _3 = Key(51)
        val _4 = Key(52)
        val _5 = Key(53)
        val _6 = Key(54)
        val _7 = Key(55)
        val _8 = Key(56)
        val _9 = Key(57)
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

private val keyMappings = mapOf(
    // The unknown key
    GlfwKey.UNKNOWN to Key.UNKNOWN,

    // Printable keys
    GlfwKey.SPACE to Key.SPACE,
    GlfwKey.APOSTROPHE to Key.APOSTROPHE,
    GlfwKey.COMMA to Key.COMMA,
    GlfwKey.MINUS to Key.MINUS,
    GlfwKey.PERIOD to Key.PERIOD,
    GlfwKey.SLASH to Key.SLASH,
    GlfwKey._0 to Key._0,
    GlfwKey._1 to Key._1,
    GlfwKey._2 to Key._2,
    GlfwKey._3 to Key._3,
    GlfwKey._4 to Key._4,
    GlfwKey._5 to Key._5,
    GlfwKey._6 to Key._6,
    GlfwKey._7 to Key._7,
    GlfwKey._8 to Key._8,
    GlfwKey._9 to Key._9,
    GlfwKey.SEMICOLON to Key.SEMICOLON,
    GlfwKey.EQUAL to Key.EQUAL,
    GlfwKey.A to Key.A,
    GlfwKey.B to Key.B,
    GlfwKey.C to Key.C,
    GlfwKey.D to Key.D,
    GlfwKey.E to Key.E,
    GlfwKey.F to Key.F,
    GlfwKey.G to Key.G,
    GlfwKey.H to Key.H,
    GlfwKey.I to Key.I,
    GlfwKey.J to Key.J,
    GlfwKey.K to Key.K,
    GlfwKey.L to Key.L,
    GlfwKey.M to Key.M,
    GlfwKey.N to Key.N,
    GlfwKey.O to Key.O,
    GlfwKey.P to Key.P,
    GlfwKey.Q to Key.Q,
    GlfwKey.R to Key.R,
    GlfwKey.S to Key.S,
    GlfwKey.T to Key.T,
    GlfwKey.U to Key.U,
    GlfwKey.V to Key.V,
    GlfwKey.W to Key.W,
    GlfwKey.X to Key.X,
    GlfwKey.Y to Key.Y,
    GlfwKey.Z to Key.Z,
    GlfwKey.LEFT_BRACKET to Key.LEFT_BRACKET,
    GlfwKey.BACKSLASH to Key.BACKSLASH,
    GlfwKey.RIGHT_BRACKET to Key.RIGHT_BRACKET,
    GlfwKey.GRAVE_ACCENT to Key.GRAVE_ACCENT,
    GlfwKey.WORLD_1 to Key.WORLD_1,
    GlfwKey.WORLD_2 to Key.WORLD_2,

    // Function keys
    GlfwKey.ESCAPE to Key.ESCAPE,
    GlfwKey.ENTER to Key.ENTER,
    GlfwKey.TAB to Key.TAB,
    GlfwKey.BACKSPACE to Key.BACKSPACE,
    GlfwKey.INSERT to Key.INSERT,
    GlfwKey.DELETE to Key.DELETE,
    GlfwKey.RIGHT to Key.RIGHT,
    GlfwKey.LEFT to Key.LEFT,
    GlfwKey.DOWN to Key.DOWN,
    GlfwKey.UP to Key.UP,
    GlfwKey.PAGE_UP to Key.PAGE_UP,
    GlfwKey.PAGE_DOWN to Key.PAGE_DOWN,
    GlfwKey.HOME to Key.HOME,
    GlfwKey.END to Key.END,
    GlfwKey.CAPS_LOCK to Key.CAPS_LOCK,
    GlfwKey.SCROLL_LOCK to Key.SCROLL_LOCK,
    GlfwKey.NUM_LOCK to Key.NUM_LOCK,
    GlfwKey.PRINT_SCREEN to Key.PRINT_SCREEN,
    GlfwKey.PAUSE to Key.PAUSE,
    GlfwKey.F1 to Key.F1,
    GlfwKey.F2 to Key.F2,
    GlfwKey.F3 to Key.F3,
    GlfwKey.F4 to Key.F4,
    GlfwKey.F5 to Key.F5,
    GlfwKey.F6 to Key.F6,
    GlfwKey.F7 to Key.F7,
    GlfwKey.F8 to Key.F8,
    GlfwKey.F9 to Key.F9,
    GlfwKey.F10 to Key.F10,
    GlfwKey.F11 to Key.F11,
    GlfwKey.F12 to Key.F12,
    GlfwKey.F13 to Key.F13,
    GlfwKey.F14 to Key.F14,
    GlfwKey.F15 to Key.F15,
    GlfwKey.F16 to Key.F16,
    GlfwKey.F17 to Key.F17,
    GlfwKey.F18 to Key.F18,
    GlfwKey.F19 to Key.F19,
    GlfwKey.F20 to Key.F20,
    GlfwKey.F21 to Key.F21,
    GlfwKey.F22 to Key.F22,
    GlfwKey.F23 to Key.F23,
    GlfwKey.F24 to Key.F24,
    GlfwKey.F25 to Key.F25,
    GlfwKey.KP_0 to Key.KP_0,
    GlfwKey.KP_1 to Key.KP_1,
    GlfwKey.KP_2 to Key.KP_2,
    GlfwKey.KP_3 to Key.KP_3,
    GlfwKey.KP_4 to Key.KP_4,
    GlfwKey.KP_5 to Key.KP_5,
    GlfwKey.KP_6 to Key.KP_6,
    GlfwKey.KP_7 to Key.KP_7,
    GlfwKey.KP_8 to Key.KP_8,
    GlfwKey.KP_9 to Key.KP_9,
    GlfwKey.KP_DECIMAL to Key.KP_DECIMAL,
    GlfwKey.KP_DIVIDE to Key.KP_DIVIDE,
    GlfwKey.KP_MULTIPLY to Key.KP_MULTIPLY,
    GlfwKey.KP_SUBTRACT to Key.KP_SUBTRACT,
    GlfwKey.KP_ADD to Key.KP_ADD,
    GlfwKey.KP_ENTER to Key.KP_ENTER,
    GlfwKey.KP_EQUAL to Key.KP_EQUAL,
    GlfwKey.LEFT_SHIFT to Key.LEFT_SHIFT,
    GlfwKey.LEFT_CONTROL to Key.LEFT_CONTROL,
    GlfwKey.LEFT_ALT to Key.LEFT_ALT,
    GlfwKey.LEFT_SUPER to Key.LEFT_SUPER,
    GlfwKey.RIGHT_SHIFT to Key.RIGHT_SHIFT,
    GlfwKey.RIGHT_CONTROL to Key.RIGHT_CONTROL,
    GlfwKey.RIGHT_ALT to Key.RIGHT_ALT,
    GlfwKey.RIGHT_SUPER to Key.RIGHT_SUPER,
    GlfwKey.MENU to Key.MENU
)

fun Key.toGlfw(): GlfwKey = keyMappings.entries.find { it.value == this }?.key ?: GlfwKey.UNKNOWN
fun Key.Companion.fromGlfw(key: GlfwKey): Key = keyMappings[key] ?: UNKNOWN

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

private val mouseButtonMappings = mapOf(
    GlfwMouseButton._1 to MouseButton.BUTTON_1,
    GlfwMouseButton._2 to MouseButton.BUTTON_2,
    GlfwMouseButton._3 to MouseButton.BUTTON_3,
    GlfwMouseButton._4 to MouseButton.BUTTON_4,
    GlfwMouseButton._5 to MouseButton.BUTTON_5,
    GlfwMouseButton._6 to MouseButton.BUTTON_6,
    GlfwMouseButton._7 to MouseButton.BUTTON_7,
    GlfwMouseButton._8 to MouseButton.BUTTON_8
)

fun MouseButton.toGlfw(): GlfwMouseButton = mouseButtonMappings.entries.find { it.value == this }!!.key
fun MouseButton.Companion.fromGlfw(button: GlfwMouseButton): MouseButton = mouseButtonMappings[button]!!
