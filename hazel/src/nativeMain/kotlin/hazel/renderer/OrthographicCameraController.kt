package hazel.renderer

import hazel.core.*
import hazel.math.MutableVec3
import hazel.math.degrees

class OrthographicCameraController(
	private var aspectRatio: Float,
	private val allowRotation: Boolean = false
) {
	private var zoomLevel: Float = 1f
	val camera = OrthographicCamera(-aspectRatio * zoomLevel, aspectRatio * zoomLevel, -zoomLevel, zoomLevel)
	private val cameraPosition = MutableVec3()
	private var cameraRotation: Float = 0f
	private val cameraTranslationSpeed: Float = 5f
	private val cameraRotationSpeed: Float = 180f.degrees


	fun onUpdate(timeStep: TimeStep) {
		Hazel.profile(::onUpdate) {
			if (Input.isKeyPressed(Key.A))
				cameraPosition.x -= cameraTranslationSpeed * timeStep.inSeconds
			else if (Input.isKeyPressed(Key.D))
				cameraPosition.x += cameraTranslationSpeed * timeStep.inSeconds

			if (Input.isKeyPressed(Key.W))
				cameraPosition.y += cameraTranslationSpeed * timeStep.inSeconds
			else if (Input.isKeyPressed(Key.S))
				cameraPosition.y -= cameraTranslationSpeed * timeStep.inSeconds

			if (allowRotation) {
				if (Input.isKeyPressed(Key.Q))
					cameraRotation += cameraRotationSpeed * timeStep.inSeconds
				else if (Input.isKeyPressed(Key.E))
					cameraRotation -= cameraRotationSpeed * timeStep.inSeconds
			}

			camera.position = cameraPosition
			camera.rotation = cameraRotation
		}
	}

	fun onEvent(event: Event) {
		Hazel.profile(::onEvent) {
			with(event) {
				dispatch(::onMouseScrolledEvent)
				dispatch(::onWindowResizeEvent)
			}
		}
	}

	private fun onMouseScrolledEvent(event: MouseScrolledEvent): Boolean {
		Hazel.profile(::onMouseScrolledEvent) {
			zoomLevel -= event.yOffset * 0.25f
			zoomLevel = zoomLevel.coerceAtLeast(0.25f)
			camera.setProjection(-aspectRatio * zoomLevel, aspectRatio * zoomLevel, -zoomLevel, zoomLevel)
		}
		return false
	}

	private fun onWindowResizeEvent(event: WindowResizeEvent): Boolean {
		Hazel.profile(::onWindowResizeEvent) {
			aspectRatio = event.width.toFloat() / event.height.toFloat()
			camera.setProjection(-aspectRatio * zoomLevel, aspectRatio * zoomLevel, -zoomLevel, zoomLevel)
		}
		return false
	}
}
