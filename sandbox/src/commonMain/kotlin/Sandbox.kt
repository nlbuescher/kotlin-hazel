import hazel.Application
import hazel.Hazel


@ExperimentalUnsignedTypes
class Sandbox : Application()


@ExperimentalUnsignedTypes
fun main() {
    Hazel.run(Sandbox())
}
