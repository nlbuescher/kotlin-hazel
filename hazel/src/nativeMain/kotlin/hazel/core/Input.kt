package hazel.core

import com.kgl.glfw.*
import com.kgl.glfw.KeyboardKey as GlfwKey
import com.kgl.glfw.MouseButton as GlfwMouseButton

object Input {
	fun isKeyPressed(key: Key): Boolean {
		val window = Hazel.application.window.nativeWindow
		val state = window.getKey(key.toGlfw())
		return state == Action.Press || state == Action.Repeat
	}

	fun isMouseButtonPressed(button: MouseButton): Boolean {
		val window = Hazel.application.window.nativeWindow
		val state = window.getMouseButton(button.toGlfw())
		return state == Action.Press
	}

	val mousePosition: Pair<Float, Float>
		get() {
			val (x, y) = Hazel.application.window.nativeWindow.cursorPosition
			return x.toFloat() to y.toFloat()
		}

	val mouseX: Float
		get() = mousePosition.first

	val mouseY: Float
		get() = mousePosition.second
}

inline class Key(val value: Int) {
	companion object {
		// The unknown key
		val UNKNOWN = Key(-1)

		// Printable keys
		val SPACE = Key(32)
		val APOSTROPHE = Key(39) // '
		val COMMA = Key(44) // ,
		val MINUS = Key(45) // -
		val PERIOD = Key(46) // .
		val SLASH = Key(47) // /
		val D0 = Key(48) // 0
		val D1 = Key(49) // 1
		val D2 = Key(50) // 2
		val D3 = Key(51) // 3
		val D4 = Key(52) // 4
		val D5 = Key(53) // 5
		val D6 = Key(54) // 6
		val D7 = Key(55) // 7
		val D8 = Key(56) // 8
		val D9 = Key(57) // 9
		val SEMICOLON = Key(59) // ;
		val EQUAL = Key(61) // =
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
		val LEFT_BRACKET = Key(91) // [
		val BACKSLASH = Key(92) // \
		val RIGHT_BRACKET = Key(93) // ]
		val GRAVE_ACCENT = Key(96) // `
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
	Key.UNKNOWN to GlfwKey.UNKNOWN,

	// Printable keys
	Key.SPACE to GlfwKey.SPACE,
	Key.APOSTROPHE to GlfwKey.APOSTROPHE,
	Key.COMMA to GlfwKey.COMMA,
	Key.MINUS to GlfwKey.MINUS,
	Key.PERIOD to GlfwKey.PERIOD,
	Key.SLASH to GlfwKey.SLASH,
	Key.D0 to GlfwKey._0,
	Key.D1 to GlfwKey._1,
	Key.D2 to GlfwKey._2,
	Key.D3 to GlfwKey._3,
	Key.D4 to GlfwKey._4,
	Key.D5 to GlfwKey._5,
	Key.D6 to GlfwKey._6,
	Key.D7 to GlfwKey._7,
	Key.D8 to GlfwKey._8,
	Key.D9 to GlfwKey._9,
	Key.SEMICOLON to GlfwKey.SEMICOLON,
	Key.EQUAL to GlfwKey.EQUAL,
	Key.A to GlfwKey.A,
	Key.B to GlfwKey.B,
	Key.C to GlfwKey.C,
	Key.D to GlfwKey.D,
	Key.E to GlfwKey.E,
	Key.F to GlfwKey.F,
	Key.G to GlfwKey.G,
	Key.H to GlfwKey.H,
	Key.I to GlfwKey.I,
	Key.J to GlfwKey.J,
	Key.K to GlfwKey.K,
	Key.L to GlfwKey.L,
	Key.M to GlfwKey.M,
	Key.N to GlfwKey.N,
	Key.O to GlfwKey.O,
	Key.P to GlfwKey.P,
	Key.Q to GlfwKey.Q,
	Key.R to GlfwKey.R,
	Key.S to GlfwKey.S,
	Key.T to GlfwKey.T,
	Key.U to GlfwKey.U,
	Key.V to GlfwKey.V,
	Key.W to GlfwKey.W,
	Key.X to GlfwKey.X,
	Key.Y to GlfwKey.Y,
	Key.Z to GlfwKey.Z,
	Key.LEFT_BRACKET to GlfwKey.LEFT_BRACKET,
	Key.BACKSLASH to GlfwKey.BACKSLASH,
	Key.RIGHT_BRACKET to GlfwKey.RIGHT_BRACKET,
	Key.GRAVE_ACCENT to GlfwKey.GRAVE_ACCENT,
	Key.WORLD_1 to GlfwKey.WORLD_1,
	Key.WORLD_2 to GlfwKey.WORLD_2,

	// Function keys
	Key.ESCAPE to GlfwKey.ESCAPE,
	Key.ENTER to GlfwKey.ENTER,
	Key.TAB to GlfwKey.TAB,
	Key.BACKSPACE to GlfwKey.BACKSPACE,
	Key.INSERT to GlfwKey.INSERT,
	Key.DELETE to GlfwKey.DELETE,
	Key.RIGHT to GlfwKey.RIGHT,
	Key.LEFT to GlfwKey.LEFT,
	Key.DOWN to GlfwKey.DOWN,
	Key.UP to GlfwKey.UP,
	Key.PAGE_UP to GlfwKey.PAGE_UP,
	Key.PAGE_DOWN to GlfwKey.PAGE_DOWN,
	Key.HOME to GlfwKey.HOME,
	Key.END to GlfwKey.END,
	Key.CAPS_LOCK to GlfwKey.CAPS_LOCK,
	Key.SCROLL_LOCK to GlfwKey.SCROLL_LOCK,
	Key.NUM_LOCK to GlfwKey.NUM_LOCK,
	Key.PRINT_SCREEN to GlfwKey.PRINT_SCREEN,
	Key.PAUSE to GlfwKey.PAUSE,
	Key.F1 to GlfwKey.F1,
	Key.F2 to GlfwKey.F2,
	Key.F3 to GlfwKey.F3,
	Key.F4 to GlfwKey.F4,
	Key.F5 to GlfwKey.F5,
	Key.F6 to GlfwKey.F6,
	Key.F7 to GlfwKey.F7,
	Key.F8 to GlfwKey.F8,
	Key.F9 to GlfwKey.F9,
	Key.F10 to GlfwKey.F10,
	Key.F11 to GlfwKey.F11,
	Key.F12 to GlfwKey.F12,
	Key.F13 to GlfwKey.F13,
	Key.F14 to GlfwKey.F14,
	Key.F15 to GlfwKey.F15,
	Key.F16 to GlfwKey.F16,
	Key.F17 to GlfwKey.F17,
	Key.F18 to GlfwKey.F18,
	Key.F19 to GlfwKey.F19,
	Key.F20 to GlfwKey.F20,
	Key.F21 to GlfwKey.F21,
	Key.F22 to GlfwKey.F22,
	Key.F23 to GlfwKey.F23,
	Key.F24 to GlfwKey.F24,
	Key.F25 to GlfwKey.F25,
	Key.KP_0 to GlfwKey.KP_0,
	Key.KP_1 to GlfwKey.KP_1,
	Key.KP_2 to GlfwKey.KP_2,
	Key.KP_3 to GlfwKey.KP_3,
	Key.KP_4 to GlfwKey.KP_4,
	Key.KP_5 to GlfwKey.KP_5,
	Key.KP_6 to GlfwKey.KP_6,
	Key.KP_7 to GlfwKey.KP_7,
	Key.KP_8 to GlfwKey.KP_8,
	Key.KP_9 to GlfwKey.KP_9,
	Key.KP_DECIMAL to GlfwKey.KP_DECIMAL,
	Key.KP_DIVIDE to GlfwKey.KP_DIVIDE,
	Key.KP_MULTIPLY to GlfwKey.KP_MULTIPLY,
	Key.KP_SUBTRACT to GlfwKey.KP_SUBTRACT,
	Key.KP_ADD to GlfwKey.KP_ADD,
	Key.KP_ENTER to GlfwKey.KP_ENTER,
	Key.KP_EQUAL to GlfwKey.KP_EQUAL,
	Key.LEFT_SHIFT to GlfwKey.LEFT_SHIFT,
	Key.LEFT_CONTROL to GlfwKey.LEFT_CONTROL,
	Key.LEFT_ALT to GlfwKey.LEFT_ALT,
	Key.LEFT_SUPER to GlfwKey.LEFT_SUPER,
	Key.RIGHT_SHIFT to GlfwKey.RIGHT_SHIFT,
	Key.RIGHT_CONTROL to GlfwKey.RIGHT_CONTROL,
	Key.RIGHT_ALT to GlfwKey.RIGHT_ALT,
	Key.RIGHT_SUPER to GlfwKey.RIGHT_SUPER,
	Key.MENU to GlfwKey.MENU
)

fun Key.toGlfw(): GlfwKey = keyMappings.getValue(this)
fun Key.Companion.fromGlfw(key: GlfwKey): Key = Key(key.value)

inline class MouseButton(val value: Int) {
	companion object {
		val BUTTON_0 = MouseButton(0)
		val BUTTON_1 = MouseButton(1)
		val BUTTON_2 = MouseButton(2)
		val BUTTON_3 = MouseButton(3)
		val BUTTON_4 = MouseButton(4)
		val BUTTON_5 = MouseButton(5)
		val BUTTON_6 = MouseButton(6)
		val BUTTON_7 = MouseButton(7)
		val BUTTON_LEFT = BUTTON_0
		val BUTTON_RIGHT = BUTTON_1
		val BUTTON_MIDDLE = BUTTON_2
	}

	override fun toString() = "$value"
}

private val mouseButtonMappings = mapOf(
	MouseButton.BUTTON_0 to GlfwMouseButton._1,
	MouseButton.BUTTON_1 to GlfwMouseButton._2,
	MouseButton.BUTTON_2 to GlfwMouseButton._3,
	MouseButton.BUTTON_3 to GlfwMouseButton._4,
	MouseButton.BUTTON_4 to GlfwMouseButton._5,
	MouseButton.BUTTON_5 to GlfwMouseButton._6,
	MouseButton.BUTTON_6 to GlfwMouseButton._7,
	MouseButton.BUTTON_7 to GlfwMouseButton._8
)

fun MouseButton.toGlfw(): GlfwMouseButton = mouseButtonMappings.getValue(this)
fun MouseButton.Companion.fromGlfw(button: GlfwMouseButton): MouseButton = MouseButton(button.value)
