import hazel.core.*
import hazel.math.*
import hazel.renderer.*
import kotlin.math.*
import kotlin.random.*

class ParticleSystem(maxParticleCount: Int = 100_000) {
	private val particlePool = Array(maxParticleCount) { Particle() }
	private var currentParticle: Int = particlePool.lastIndex

	fun onUpdate(timeStep: TimeStep) {
		for (particle in particlePool) {
			if (!particle.isActive) continue

			if (particle.lifeRemaining <= 0f) {
				particle.isActive = false
				continue
			}

			particle.lifeRemaining -= timeStep.inSeconds
			particle.position.plusAssign(particle.velocity * timeStep.inSeconds)
			particle.rotation += 0.01f * timeStep.inSeconds
		}
	}

	fun onRender(camera: OrthographicCamera) {
		Renderer2D.beginScene(camera)

		for (particle in particlePool) {
			if (!particle.isActive) continue

			val life = particle.lifeRemaining / particle.lifeTime
			val color = lerp(particle.endColor, particle.startColor, life)
			val size = lerp(particle.endSize, particle.startSize, life)
			val position = Vec3(particle.position.x, particle.position.y, 0.2f)
			Renderer2D.drawRotatedQuad(position, Vec2(size), particle.rotation, color)
		}

		Renderer2D.endScene()
	}

	fun emit(
		position: Vec2,
		velocity: Vec2,
		startColor: Vec4,
		endColor: Vec4,
		startSize: Float,
		endSize: Float,
		velocityVariation: Vec2 = Vec2(),
		sizeVariation: Float = 0f,
		lifeTime: Float = 1f
	) {
		particlePool[currentParticle].also {
			it.isActive = true
			it.position = position.copy()
			it.rotation = Random.nextFloat() * 2 * PI.toFloat()

			it.velocity = Vec2(
				velocity.x + velocityVariation.x * (Random.nextFloat() - 0.5f),
				velocity.y + velocityVariation.y * (Random.nextFloat() - 0.5f)
			)

			it.startColor = startColor.copy()
			it.endColor = endColor.copy()

			it.lifeTime = lifeTime
			it.lifeRemaining = lifeTime
			it.startSize = startSize + sizeVariation * (Random.nextFloat() - 0.5f)
			it.endSize = endSize
		}
		currentParticle -= 1

		if (currentParticle < 0) currentParticle = particlePool.lastIndex
	}

	private class Particle {
		var position: Vec2 = Vec2()
		var velocity: Vec2 = Vec2()
		var startColor: Vec4 = Vec4()
		var endColor: Vec4 = Vec4()
		var rotation: Float = 0f
		var startSize: Float = 0f
		var endSize: Float = 0f

		var lifeTime: Float = 0f
		var lifeRemaining: Float = 0f

		var isActive: Boolean = false
	}
}
