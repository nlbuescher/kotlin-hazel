package hazel.renderer

import hazel.core.*
import hazel.events.Event
import hazel.events.MouseScrolledEvent
import hazel.events.WindowResizeEvent
import hazel.math.MutableVec3
import hazel.math.degrees
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class OrthographicCameraController(
	private var aspectRatio: Float,
	private val allowRotation: Boolean = false
) {
	private var zoomLevel: Float = 1f
	val camera = OrthographicCamera(-aspectRatio * zoomLevel, aspectRatio * zoomLevel, -zoomLevel, zoomLevel)
	private val cameraPosition = MutableVec3()
	private var cameraRotation: Float = 0f
	private var cameraTranslationSpeed: Float = zoomLevel
	private val cameraRotationSpeed: Float = 180f.degrees


	fun onUpdate(timeStep: TimeStep) {
		Hazel.profile("OrthographicCameraController.onUpdate(TimeStep)") {
			if (Input.isKeyPressed(Key.A)) {
				cameraPosition.x -= cos(cameraRotation) * cameraTranslationSpeed * timeStep.inSeconds
				cameraPosition.y -= sin(cameraRotation) * cameraTranslationSpeed * timeStep.inSeconds
			} else if (Input.isKeyPressed(Key.D)) {
				cameraPosition.x += cos(cameraRotation) * cameraTranslationSpeed * timeStep.inSeconds
				cameraPosition.x += sin(cameraRotation) * cameraTranslationSpeed * timeStep.inSeconds
			}

			if (Input.isKeyPressed(Key.W)) {
				cameraPosition.x += -sin(cameraRotation) * cameraTranslationSpeed * timeStep.inSeconds
				cameraPosition.y += cos(cameraRotation) * cameraTranslationSpeed * timeStep.inSeconds
			} else if (Input.isKeyPressed(Key.S)) {
				cameraPosition.x -= -sin(cameraRotation) * cameraTranslationSpeed * timeStep.inSeconds
				cameraPosition.y -= cos(cameraRotation) * cameraTranslationSpeed * timeStep.inSeconds
			}

			if (allowRotation) {
				if (Input.isKeyPressed(Key.Q)) {
					cameraRotation += cameraRotationSpeed * timeStep.inSeconds
				} else if (Input.isKeyPressed(Key.E)) {
					cameraRotation -= cameraRotationSpeed * timeStep.inSeconds
				}

				if (cameraRotation > 180f.degrees) {
					cameraRotation -= 360f.degrees
				} else if (cameraRotation <= (-180f).degrees) {
					cameraRotation += 360f.degrees
				}
				camera.rotation = cameraRotation
			}

			camera.position = cameraPosition
		}
	}

	fun onEvent(event: Event) {
		Hazel.profile("OrthographicCameraController.onEvent(Event)") {
			with(event) {
				dispatch(::onMouseScrolledEvent)
				dispatch(::onWindowResizeEvent)
			}
		}
	}

	private fun onMouseScrolledEvent(event: MouseScrolledEvent): Boolean {
		Hazel.profile("OrthographicCameraController.onMouseScrolledEvent(MouseScrolledEvent): Boolean") {
			zoomLevel -= event.yOffset * 0.25f
			zoomLevel = zoomLevel.coerceAtLeast(0.25f)
			cameraTranslationSpeed = zoomLevel
			camera.setProjection(-aspectRatio * zoomLevel, aspectRatio * zoomLevel, -zoomLevel, zoomLevel)
		}
		return false
	}

	private fun onWindowResizeEvent(event: WindowResizeEvent): Boolean {
		Hazel.profile("OrthographicCameraController.onWindowResizeEvent(WindowResizeEvent): Boolean") {
			aspectRatio = event.width.toFloat() / event.height.toFloat()
			camera.setProjection(-aspectRatio * zoomLevel, aspectRatio * zoomLevel, -zoomLevel, zoomLevel)
		}
		return false
	}
}
