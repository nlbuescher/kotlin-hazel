package hazel.scene

import hazel.math.*
import hazel.renderer.*

class SceneCamera : Camera() {
	private var _projectionType: ProjectionType = ProjectionType.Orthographic

	private var _perspectiveFov: Float = 45f.degrees
	private var _perspectiveNear: Float = 0.01f
	private var _perspectiveFar: Float = 1000f

	private var _orthographicSize: Float = 10f
	private var _orthographicNear: Float = -1f
	private var _orthographicFar: Float = 1f

	private var aspectRatio: Float = 0f

	init {
		recalculateProjection()
	}


	fun setPerspective(verticalFOV: Float, nearClip: Float, farClip: Float) {
		_projectionType = ProjectionType.Perspective
		_perspectiveFov = verticalFOV
		_perspectiveNear = nearClip
		_perspectiveFar = farClip
		recalculateProjection()
	}

	fun setOrthographic(size: Float, nearClip: Float, farClip: Float) {
		_projectionType = ProjectionType.Orthographic
		_orthographicSize = size
		_orthographicNear = nearClip
		_orthographicFar = farClip
		recalculateProjection()
	}


	fun setViewportSize(width: Int, height: Int) {
		aspectRatio = width.toFloat() / height.toFloat()
		recalculateProjection()
	}


	var perspectiveVerticalFov: Float
		get() = _perspectiveFov
		set(value) {
			_perspectiveFov = value
			recalculateProjection()
		}
	var perspectiveNearClip: Float
		get() = _perspectiveNear
		set(value) {
			_perspectiveNear = value
			recalculateProjection()
		}
	var perspectiveFarClip: Float
		get() = _perspectiveFar
		set(value) {
			_perspectiveFar = value
			recalculateProjection()
		}

	var orthographicSize: Float
		get() = _orthographicSize
		set(value) {
			_orthographicSize = value
			recalculateProjection()
		}
	var orthographicNearClip: Float
		get() = _orthographicNear
		set(value) {
			_orthographicNear = value
			recalculateProjection()
		}
	var orthographicFarClip: Float
		get() = _orthographicFar
		set(value) {
			_orthographicFar = value
			recalculateProjection()
		}

	var projectionType: ProjectionType
		get() = _projectionType
		set(value) {
			_projectionType = value
			recalculateProjection()
		}


	private fun recalculateProjection() {
		_projection = when (_projectionType) {
			ProjectionType.Perspective -> {
				perspectiveProjectionOf(
					_perspectiveFov, aspectRatio, perspectiveNearClip, perspectiveFarClip
				)
			}
			ProjectionType.Orthographic -> {
				val left = -0.5f * aspectRatio * _orthographicSize
				val right = 0.5f * aspectRatio * _orthographicSize
				val bottom = -0.5f * _orthographicSize
				val top = 0.5f * _orthographicSize
				orthographicProjectionOf(
					left, right, bottom, top,
					orthographicNearClip, orthographicFarClip
				)
			}
		}
	}


	enum class ProjectionType {
		Perspective,
		Orthographic
	}
}
