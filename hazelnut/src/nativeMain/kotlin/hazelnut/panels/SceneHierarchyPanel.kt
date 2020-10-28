package hazelnut.panels

import com.imgui.*
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
					val transform = entity.getComponent<TransformComponent>().transform
					dragFloat3("Position", transform[3], 0.1f)

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
		}
	}
}
