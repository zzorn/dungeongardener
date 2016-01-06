package org.dungeongardener.util

/**
 *
 */
data class Int2(var x: Int = 0,
                var y: Int = 0) {

    constructor(other: Int2): this(other.x, other.y)

    fun zero() {
        x = 0
        y = 0
    }

    fun set(x: Int = 0, y: Int = 0) {
        this.x = x
        this.y = y
    }

    fun add(other: Int2) {
        x += other.x
        y += other.y
    }

    fun sub(other: Int2) {
        x -= other.x
        y -= other.y
    }

    fun scale(i: Int) {
        x *= i
        y *= i
    }

    fun scale(other: Int2) {
        x *= other.x
        y *= other.y
    }

    fun divide(i: Int) {
        x /= i
        y /= i
    }

    operator fun plus(other: Int2): Int2 =
            Int2(x + other.x,
                    y + other.y)

    operator fun minus(other: Int2): Int2 =
            Int2(x - other.x,
                    y - other.y)

    operator fun times(other: Int2): Int2 =
            Int2(x * other.x,
                    y * other.y)

    operator fun div(other: Int2): Int2 =
            Int2(x / other.x,
                    y / other.y)

    operator fun times(scalar: Int): Int2 =
            Int2(x * scalar,
                    y * scalar)

    operator fun div(scalar: Int): Int2 =
            Int2(x / scalar,
                    y / scalar)

    fun gridOrigo(gridSize: Int2): Int2 {
        val v = floorDiv(gridSize)
        v.scale(gridSize)
        return v
    }

    fun floorDiv(other: Int2): Int2 {
        fun floorDiv(v: Int, size: Int): Int = (v / size) - if (v < 0) 1 else 0
        return Int2(floorDiv(x, other.x),
                floorDiv(y, other.y))
    }

    fun inRange(ends: Int2): Boolean = inRange(endX = ends.x,
            endY = ends.y)

    fun inRange(starts: Int2, ends: Int2): Boolean = inRange(
            starts.x, starts.y,
            ends.x, ends.y)

    fun inRange(startX: Int = 0,
                startY: Int = 0,
                endX: Int = 0,
                endY: Int = 0): Boolean =
            x in startX .. endX-1 &&
                    y in startY .. endY-1

    fun scaleSum(xScale: Int = 1, yScale: Int = 1): Int = x * xScale + y * yScale

    fun multiplyAll(xOffset: Int = 0, yOffset: Int = 0): Int = (x+xOffset) * (y+yOffset)

//    fun toVector2(): Vector2 = Vector2(x.toFloat(), y.toFloat())

    fun toInt3(z: Int = 0): Int3  = Int3(x, y, z)

}

/*
fun Vector2.toInt2Rounded(): Int2 =
        Int2(MathUtils.round(this.x),
                MathUtils.round(this.y))

fun Vector2.toInt2Floored(): Int2 =
        Int2(MathUtils.fastFloor(this.x),
                MathUtils.fastFloor(this.y))

*/