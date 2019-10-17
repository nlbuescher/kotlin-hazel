package hazel.math

class Float4(var x: Float, var y: Float, var z: Float, var w: Float) {
    var r: Float
        get() = x
        set(new) = run { x = new }

    var g: Float
        get() = y
        set(new) = run { y = new }

    var b: Float
        get() = z
        set(new) = run { z = new }

    var a: Float
        get() = w
        set(new) = run { w = new }
}
