package hazel.core

import hazel.core.TimeStepUnit.SECONDS
import hazel.renderer.Renderer

abstract class Application : Disposable {
	val window: Window

	private var isRunning: Boolean = true
	private var isMinimized: Boolean = false

	private val layerStack: LayerStack
	private val imGuiLayer: ImGuiLayer

	private var lastFrameTime: Float = 0f

	init {
		val profiler = Hazel.Profiler("hazel.core.Application.<init>()")
		profiler.start()

		window = Window().apply { setEventCallback(::onEvent) }

		layerStack = LayerStack()
		imGuiLayer = ImGuiLayer()

		Renderer.init()

		profiler.stop()
	}

	override fun dispose() {
		Hazel.profile(::dispose) {
			Renderer.shutdown()
			window.dispose()
		}
	}

	fun addLayer(layer: Layer) {
		Hazel.profile(::addLayer) {
			layerStack.add(layer)
			layer.onAttach()
		}
	}

	fun addOverlay(overlay: Overlay) {
		Hazel.profile(::addOverlay) {
			layerStack.add(overlay)
			overlay.onAttach()
		}
	}

	fun run() {
		Hazel.profile(::run) {
			// don't add imGuiLayer to layer stack until run because ImGuiLayer requires Hazel.application to be set
			addOverlay(imGuiLayer)

			while (isRunning) {
				Hazel.profile("Run Loop") {
					val time = Hazel.getTime()
					val timeStep = (time - lastFrameTime).toTimeStep(SECONDS)
					lastFrameTime = time

					if (!isMinimized) {
						Hazel.profile("LayerStack onUpdate") {
							layerStack.forEach { it.onUpdate(timeStep) }
						}

						imGuiLayer.begin()
						Hazel.profile("LayerStack onImGuiRender") {
							layerStack.forEach { it.onImGuiRender() }
						}
						imGuiLayer.end()
					}

					window.onUpdate()
				}
			}
		}
	}

	fun onEvent(event: Event) {
		Hazel.coreDebug { event }
		Hazel.profile(::onEvent) {
			event.dispatch(::onWindowResize)
			event.dispatch(::onWindowClose)

			for (layer in layerStack.reversed()) {
				layer.onEvent(event)
				if (event.isHandled) break
			}
		}
	}

	private fun onWindowResize(event: WindowResizeEvent): Boolean {
		return Hazel.profile(::onWindowResize) {
			if (event.width == 0 || event.height == 0) {
				isMinimized = true
				return@profile true
			}

			isMinimized = false

			Renderer.onWindowResize(event.width, event.height)

			return@profile false
		}
	}

	@Suppress("UNUSED_PARAMETER")
	private fun onWindowClose(event: WindowCloseEvent): Boolean {
		isRunning = false
		return true
	}
}
