package hazel.imgui

import com.imgui.*
import hazel.math.*
import hazel.math.Vec4

fun ImGui.colorEdit4(label: String, color: Vec4) {
	val array = color.toFloatArray()
	colorEdit4(label, array)
	array.forEachIndexed { i, it -> color[i] = it }
}

fun ImGui.dragFloat3(
	label: String,
	floats: Vec4,
	vSpeed: Float = 1f,
	vMin: Float = 0f,
	vMax: Float = 0f,
	format: String = "%.3f",
	flags: Flag<ImGuiSliderFlags>? = null,
) {
	val array = floats.toFloatArray()
	dragFloat3(label, array, vSpeed, vMin, vMax, format, flags)
	array.forEachIndexed { i, it -> floats[i] = it }
}
