package hazel.imgui

import cimgui.internal.*
import cimgui.internal.ImVec2
import com.imgui.*
import com.imgui.ImGuiSliderFlags
import com.imgui.Vec2
import hazel.math.*
import hazel.math.Vec4
import kotlinx.cinterop.*

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

@Suppress("unused")
fun ImGui.getContentRegionAvail(): Vec2 = memScoped {
	val result = alloc<ImVec2>()
	igGetContentRegionAvail(result.ptr)
	Vec2(result.x, result.y)
}
