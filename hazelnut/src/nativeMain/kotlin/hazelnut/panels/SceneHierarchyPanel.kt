package hazelnut.panels

import cimgui.internal.*
import com.imgui.*
import com.imgui.ImFont
import com.imgui.ImGuiCol
import com.imgui.ImGuiMouseButton
import com.imgui.ImGuiPopupFlags
import com.imgui.ImGuiStyleVar
import com.imgui.ImGuiTreeNodeFlags
import com.imgui.Vec2
import hazel.ecs.*
import hazel.imgui.*
import hazel.math.*
import hazel.scene.*
import kotlinx.cinterop.*
import kotlin.math.*
import kotlin.reflect.*

class SceneHierarchyPanel(var context: Scene) {
	private var selectionContext: Entity? = null

	fun onImGuiRender() {
		with(ImGui) {
			begin("Scene Hierarchy")

			context.forEach {
				drawEntityNode(it)
			}

			if (isMouseDown(ImGuiMouseButton.Left) && isWindowHovered()) {
				selectionContext = null
			}

			// right click on blank space
			if (beginPopupContextWindow(popupFlags = ImGuiPopupFlags.MouseButtonRight + ImGuiPopupFlags.NoOpenOverItems)) {
				if (menuItem("Create Empty Entity")) {
					context.createEntity("Empty Entity")
				}
				endPopup()
			}

			end() // Scene Hierarchy


			begin("Properties")

			selectionContext?.let { selected ->
				drawComponents(selected)
			}

			end()
		}
	}

	private fun drawEntityNode(entity: Entity) {
		with(ImGui) {
			val tag = entity.getComponent<TagComponent>().tag

			val flags = if (selectionContext == entity) {
				ImGuiTreeNodeFlags.Selected +
					ImGuiTreeNodeFlags.OpenOnArrow +
					ImGuiTreeNodeFlags.SpanAvailWidth
			} else {
				ImGuiTreeNodeFlags.OpenOnArrow +
					ImGuiTreeNodeFlags.SpanAvailWidth
			}
			val isOpen = treeNodeEx("$entity", flags, tag)
			if (isItemClicked()) {
				selectionContext = entity
			}

			var isDeleted = false
			if (beginPopupContextItem()) {
				if (menuItem("Delete Entity")) {
					isDeleted = true
				}
				endPopup()
			}

			if (isOpen) {
				treePop()
			}

			if (isDeleted) {
				context.destroyEntity(entity)
				if (selectionContext == entity) {
					selectionContext = null
				}
			}
		}
	}

	// necessary because variable references aren't supported yet
	private var floatTemp: Float = 0f

	private fun drawVec3Control(label: String, values: Vec3, resetValue: Float = 0f, columnWidth: Float = 100f) {
		with(ImGui) {
			val io = getIO()
			val boldFont = ImFont(io.fonts!!.ptr.pointed.Fonts.Data!![0]!!)

			pushID(label)

			columns(2)
			setColumnWidth(0, columnWidth)
			text(label)
			nextColumn()

			igPushMultiItemsWidths(3, calcItemWidth())
			pushStyleVar(ImGuiStyleVar.ItemSpacing, Vec2(0f, 0f))

			val lineHeight = getFontSize() + getStyle().framePadding.y * 2f
			val buttonSize = Vec2(lineHeight + 3f, lineHeight)

			pushStyleColor(ImGuiCol.Button, com.imgui.Vec4(0.8f, 0.1f, 0.15f, 1f))
			pushStyleColor(ImGuiCol.ButtonHovered, com.imgui.Vec4(0.9f, 0.2f, 0.2f, 1f))
			pushStyleColor(ImGuiCol.ButtonActive, com.imgui.Vec4(0.8f, 0.1f, 0.15f, 1f))
			pushFont(boldFont)
			if (button("X", buttonSize)) {
				values.x = resetValue
			}
			popFont()
			popStyleColor(3)

			sameLine()
			dragFloat("##X", values::x, vSpeed = 0.1f, format = "%.2f")
			popItemWidth()
			sameLine()

			pushStyleColor(ImGuiCol.Button, com.imgui.Vec4(0.2f, 0.7f, 0.2f, 1f))
			pushStyleColor(ImGuiCol.ButtonHovered, com.imgui.Vec4(0.3f, 0.8f, 0.3f, 1f))
			pushStyleColor(ImGuiCol.ButtonActive, com.imgui.Vec4(0.2f, 0.7f, 0.2f, 1f))
			pushFont(boldFont)
			if (button("Y", buttonSize)) {
				values.y = resetValue
			}
			popFont()
			popStyleColor(3)

			sameLine()
			dragFloat("##Y", values::y, vSpeed = 0.1f, format = "%.2f")
			popItemWidth()
			sameLine()

			pushStyleColor(ImGuiCol.Button, com.imgui.Vec4(0.1f, 0.25f, 0.8f, 1f))
			pushStyleColor(ImGuiCol.ButtonHovered, com.imgui.Vec4(0.2f, 0.35f, 0.9f, 1f))
			pushStyleColor(ImGuiCol.ButtonActive, com.imgui.Vec4(0.1f, 0.25f, 0.8f, 1f))
			pushFont(boldFont)
			if (button("Z", buttonSize)) {
				values.z = resetValue
			}
			popFont()
			popStyleColor(3)

			sameLine()
			dragFloat("##Z", values::z, vSpeed = 0.1f, format = "%.2f")
			popItemWidth()

			popStyleVar()

			columns(1)

			popID()
		}
	}

	private inline fun <reified T : Any> drawComponent(
		name: String,
		entity: Entity,
		noinline block: (T) -> Unit
	) {
		drawComponent(T::class, name, entity, block)
	}

	private fun <T : Any> drawComponent(
		type: KClass<T>,
		name: String,
		entity: Entity,
		block: (T) -> Unit
	) {
		val treeNodeFlags: Flag<ImGuiTreeNodeFlags> =
			ImGuiTreeNodeFlags.DefaultOpen +
				ImGuiTreeNodeFlags.Framed +
				ImGuiTreeNodeFlags.SpanAvailWidth +
				ImGuiTreeNodeFlags.AllowItemOverlap +
				ImGuiTreeNodeFlags.FramePadding

		if (entity.hasComponent(type)) {
			val component = entity.getComponent(type)
			val contentRegionAvailable = ImGui.getContentRegionAvail()

			ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, Vec2(4f, 4f))
			val lineHeight = ImGui.getFont().fontSize + ImGui.getStyle().framePadding.y * 2f
			ImGui.separator()
			val isOpen = ImGui.treeNodeEx("${type.typeInfo.id}", treeNodeFlags, name)
			ImGui.popStyleVar()
			ImGui.sameLine(contentRegionAvailable.x - lineHeight / 2f)
			if (ImGui.button("+", Vec2(lineHeight, lineHeight))) {
				ImGui.openPopup("ComponentSettings")
			}

			var isRemoved = false
			if (ImGui.beginPopup("ComponentSettings")) {
				if (ImGui.menuItem("Remove Component")) {
					isRemoved = true
				}
				ImGui.endPopup()
			}

			if (isOpen) {
				block(component)
				ImGui.treePop()
			}

			if (isRemoved) {
				entity.removeComponent(type)
			}
		}
	}

	private fun drawComponents(entity: Entity) {
		with(ImGui) {
			if (entity.hasComponent<TagComponent>()) {
				val tag = entity.getComponent<TagComponent>()

				val buffer = ByteArray(256)
				tag.tag.encodeToByteArray().apply {
					copyInto(buffer, endIndex = min(size, buffer.size))
				}
				if (inputText("##Tag", buffer)) {
					tag.tag = buffer.decodeToString()
				}
			}

			sameLine()
			pushItemWidth(-1f)

			if (button("Add Component")) {
				openPopup("AddComponent")
			}
			if (beginPopup("AddComponent")) {
				if (menuItem("Camera")) {
					selectionContext?.addComponent(CameraComponent())
					closeCurrentPopup()
				}

				if (menuItem("Sprite Renderer")) {
					selectionContext?.addComponent(SpriteRendererComponent())
					closeCurrentPopup()
				}

				endPopup()
			}

			popItemWidth()

			drawComponent<TransformComponent>("Transform", entity) { component ->
				drawVec3Control("Translation", component.translation)
				val rotation = with(component.rotation) { Vec3(x.radians, y.radians, z.radians) }
				drawVec3Control("Rotation", rotation)
				with(component.rotation) { x = rotation.x.degrees; y = rotation.y.degrees; z = rotation.z.degrees }
				drawVec3Control("Scale", component.scale, 1f)
			}

			drawComponent<CameraComponent>("Camera", entity) { component ->
				val camera = component.camera

				checkbox("Primary", component::isPrimary)

				if (beginCombo("Projection", camera.projectionType.name)) {
					SceneCamera.ProjectionType.values().forEach {
						val isSelected = it == camera.projectionType
						if (selectable(it.name, isSelected)) {
							camera.projectionType = it
						}

						if (isSelected) {
							setItemDefaultFocus()
						}
					}
					endCombo()
				}

				when (camera.projectionType) {
					SceneCamera.ProjectionType.Perspective -> {
						floatTemp = camera.perspectiveVerticalFov.radians
						if (dragFloat("Vertical FOV", ::floatTemp)) {
							camera.perspectiveVerticalFov = floatTemp.degrees
						}
						dragFloat("Near", camera::perspectiveNearClip)
						dragFloat("Far", camera::perspectiveFarClip)
					}
					SceneCamera.ProjectionType.Orthographic -> {
						dragFloat("Size", camera::orthographicSize)
						dragFloat("Near", camera::orthographicNearClip)
						dragFloat("Far", camera::orthographicFarClip)
						checkbox("Fixed Aspect Ratio", component::hasFixedAspectRatio)
					}
				}
			}

			drawComponent<SpriteRendererComponent>("Sprite Renderer", entity) { component ->
				colorEdit4("Color", component.color)
			}
		}
	}
}
