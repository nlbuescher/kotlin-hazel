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
		val profiler = Hazel.Profiler("Application(): Application")
		profiler.start()

		window = Window().apply { setEventCallback(::onEvent) }

		layerStack = LayerStack()
		imGuiLayer = ImGuiLayer()

		Renderer.init()

		profiler.stop()
	}

	override fun dispose() {
		Hazel.profile("Application.dispose()") {
			Renderer.shutdown()
			window.dispose()
		}
	}

	fun addLayer(layer: Layer) {
		Hazel.profile("Application.addLayer(Layer)") {
			layerStack.add(layer)
			layer.onAttach()
		}
	}

	fun addOverlay(overlay: Overlay) {
		Hazel.profile("Application.addOverlay(Overlay)") {
			layerStack.add(overlay)
			overlay.onAttach()
		}
	}

	fun run() {
		Hazel.profile("Application.run()") {
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
		Hazel.profile("Application.onEvent(Event)") {
			event.dispatch(::onWindowResize)
			event.dispatch(::onWindowClose)

			for (layer in layerStack.reversed()) {
				layer.onEvent(event)
				if (event.isHandled) break
			}
		}
	}

	private fun onWindowResize(event: WindowResizeEvent): Boolean {
		return Hazel.profile("Application.onWindowResize(WindowResizeEvent): Boolean") {
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
