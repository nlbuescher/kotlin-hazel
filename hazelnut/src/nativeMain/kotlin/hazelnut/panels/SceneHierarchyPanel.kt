package hazelnut.panels

import cimgui.internal.*
import com.imgui.*
import com.imgui.ImGuiCol
import com.imgui.ImGuiMouseButton
import com.imgui.ImGuiStyleVar
import com.imgui.ImGuiTreeNodeFlags
import hazel.imgui.*
import hazel.math.*
import hazel.scene.*
import kotlin.math.*

class SceneHierarchyPanel(var context: Scene) {
	private var selectionContext: Scene.Entity? = null

	fun onImGuiRender() {
		with(ImGui) {
			begin("Scene Hierarchy")

			context.forEach(::drawEntityNode)

			if (isMouseDown(ImGuiMouseButton.Left) && isWindowHovered()) {
				selectionContext = null
			}

			end() // Scene Hierarchy


			begin("Properties")

			selectionContext?.let {
				drawComponents(it)
			}

			end()
		}
	}

	private fun drawEntityNode(entity: Scene.Entity) {
		with(ImGui) {
			val tag = entity.getComponent<TagComponent>().tag

			val flags = if (selectionContext == entity) {
				ImGuiTreeNodeFlags.Selected + ImGuiTreeNodeFlags.OpenOnArrow
			} else {
				ImGuiTreeNodeFlags.OpenOnArrow
			}
			val isOpen = treeNodeEx("$entity", flags, tag)
			if (isItemClicked()) {
				selectionContext = entity
			}

			if (isOpen) {
				treePop()
			}
		}
	}

	// necessary because variable references aren't supported yet
	private var floatTemp: Float = 0f

	private fun drawVec3Control(label: String, values: Vec3, resetValue: Float = 0f, columnWidth: Float = 100f) {
		with(ImGui) {
			pushID(label)

			columns(2)
			setColumnWidth(0, columnWidth)
			text(label)
			nextColumn()

			igPushMultiItemsWidths(3, calcItemWidth())
			pushStyleVar(ImGuiStyleVar.ItemSpacing, com.imgui.Vec2(0f, 0f))

			val lineHeight = getFontSize() + getStyle().framePadding.y * 2f
			val buttonSize = com.imgui.Vec2(lineHeight + 3f, lineHeight)

			pushStyleColor(ImGuiCol.Button, com.imgui.Vec4(0.8f, 0.1f, 0.15f, 1f))
			pushStyleColor(ImGuiCol.ButtonHovered, com.imgui.Vec4(0.9f, 0.2f, 0.2f, 1f))
			pushStyleColor(ImGuiCol.ButtonActive, com.imgui.Vec4(0.8f, 0.1f, 0.15f, 1f))
			if (button("X", buttonSize)) {
				values.x = resetValue
			}
			popStyleColor(3)

			sameLine()
			dragFloat("##X", values::x, vSpeed = 0.1f, format = "%.2f")
			popItemWidth()
			sameLine()

			pushStyleColor(ImGuiCol.Button, com.imgui.Vec4(0.2f, 0.7f, 0.2f, 1f))
			pushStyleColor(ImGuiCol.ButtonHovered, com.imgui.Vec4(0.3f, 0.8f, 0.3f, 1f))
			pushStyleColor(ImGuiCol.ButtonActive, com.imgui.Vec4(0.2f, 0.7f, 0.2f, 1f))
			if (button("Y", buttonSize)) {
				values.y = resetValue
			}
			popStyleColor(3)

			sameLine()
			dragFloat("##Y", values::y, vSpeed = 0.1f, format = "%.2f")
			popItemWidth()
			sameLine()

			pushStyleColor(ImGuiCol.Button, com.imgui.Vec4(0.1f, 0.25f, 0.8f, 1f))
			pushStyleColor(ImGuiCol.ButtonHovered, com.imgui.Vec4(0.2f, 0.35f, 0.9f, 1f))
			pushStyleColor(ImGuiCol.ButtonActive, com.imgui.Vec4(0.1f, 0.25f, 0.8f, 1f))
			if (button("Z", buttonSize)) {
				values.z = resetValue
			}
			popStyleColor(3)

			sameLine()
			dragFloat("##Z", values::z, vSpeed = 0.1f, format = "%.2f")
			popItemWidth()

			popStyleVar()

			columns(1)

			popID()
		}
	}

	private fun drawComponents(entity: Scene.Entity) {
		with(ImGui) {
			if (entity.hasComponent<TagComponent>()) {
				val tag = entity.getComponent<TagComponent>()

				val buffer = ByteArray(256)
				tag.tag.encodeToByteArray().apply {
					copyInto(buffer, endIndex = min(size, buffer.size))
				}
				if (inputText("Tag", buffer)) {
					tag.tag = buffer.decodeToString()
				}
			}

			if (entity.hasComponent<TransformComponent>()) {
				if (treeNodeEx("TransformComponent", ImGuiTreeNodeFlags.DefaultOpen, "Transform")) {
					val component = entity.getComponent<TransformComponent>()
					drawVec3Control("Translation", component.translation)
					val rotation = with(component.rotation) { Vec3(x.radians, y.radians, z.radians) }
					drawVec3Control("Rotation", rotation)
					with(component.rotation) { x = rotation.x.degrees; y = rotation.y.degrees; z = rotation.z.degrees }
					drawVec3Control("Scale", component.scale)

					treePop()
				}
			}

			if (entity.hasComponent<CameraComponent>()) {
				if (treeNodeEx("CameraComponent", ImGuiTreeNodeFlags.DefaultOpen, "Camera")) {
					val cameraComponent = entity.getComponent<CameraComponent>()
					val camera = cameraComponent.camera

					checkbox("Primary", cameraComponent::isPrimary)

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
							checkbox("Fixed Aspect Ratio", cameraComponent::hasFixedAspectRatio)
						}
					}
					treePop()
				}
			}

			if (entity.hasComponent<SpriteRendererComponent>()) {
				if (treeNodeEx("SpriteRendererComponent", ImGuiTreeNodeFlags.DefaultOpen, "Sprite Renderer")) {
					val color = entity.getComponent<SpriteRendererComponent>().color
					colorEdit4("Color", color)

					treePop()
				}
			}
		}
	}
}
