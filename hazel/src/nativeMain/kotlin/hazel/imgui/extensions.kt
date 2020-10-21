package hazel.imgui

import com.imgui.*
import hazel.math.Vec4

fun ImGui.colorEdit4(label: String, color: Vec4) {
	val array = FloatArray(4) { color[it] }
	colorEdit4(label, array)
	for(i in 0 until 4) {
		color[i] = array[i]
	}
}
