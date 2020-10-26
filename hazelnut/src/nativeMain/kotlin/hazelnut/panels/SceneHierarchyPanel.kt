package hazelnut.panels

import com.imgui.*
import hazel.scene.*

class SceneHierarchyPanel(var context: Scene) {
	private var selectionContext: Scene.Entity? = null

	fun onImGuiRender() {
		with(ImGui) {
			begin("Scene Hierarchy")
			context.forEach(::drawEntityNode)
			end() // Scene Hierarchy
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
}
