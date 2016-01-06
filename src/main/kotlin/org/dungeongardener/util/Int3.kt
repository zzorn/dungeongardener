package org.dungeongardener.util

/**
 *
 */
data class Int3(var x: Int = 0,
                var y: Int = 0,
                var z: Int = 0) {

    constructor(other: Int3): this(other.x, other.y, other.z)
    constructor(other: Int2, z: Int): this(other.x, other.y, z)

    fun zero() {
        x = 0
        y = 0
        z = 0
    }

    fun set(other: Int3) {
        this.x = other.x
        this.y = other.y
        this.z = other.z
    }

    fun set(x: Int = 0, y: Int = 0, z: Int = 0) {
        this.x = x
        this.y = y
        this.z = z
    }

    fun add(other: Int3) {
        x += other.x
        y += other.y
        z += other.z
    }

    fun sub(other: Int3) {
        x -= other.x
        y -= other.y
        z -= other.z
    }

    fun scale(other: Int3) {
        x *= other.x
        y *= other.y
        z *= other.z
    }

    operator fun plus(other: Int3): Int3 =
            Int3(x + other.x,
                    y + other.y,
                    z + other.z)

    operator fun minus(other: Int3): Int3 =
            Int3(x - other.x,
                    y - other.y,
                    z - other.z)

    operator fun times(other: Int3): Int3 =
            Int3(x * other.x,
                    y * other.y,
                    z * other.z)

    operator fun div(other: Int3): Int3 =
            Int3(x / other.x,
                    y / other.y,
                    z / other.z)

    operator fun times(scalar: Int): Int3 =
            Int3(x * scalar,
                    y * scalar,
                    z * scalar)

    operator fun div(scalar: Int): Int3 =
            Int3(x / scalar,
                    y / scalar,
                    z / scalar)

    fun gridOrigo(gridSize: Int3): Int3 {
        val v = floorDiv(gridSize)
        v.scale(gridSize)
        return v
    }

    fun floorDiv(other: Int3): Int3 {
        fun floorDiv(v: Int, size: Int): Int = (v / size) - if (v < 0) 1 else 0
        return Int3(floorDiv(x, other.x),
                floorDiv(y, other.y),
                floorDiv(z, other.z))
    }

    fun inRange(ends: Int3): Boolean = inRange(endX = ends.x,
            endY = ends.y,
            endZ = ends.z)

    fun inRange(starts: Int3, ends: Int3): Boolean = inRange(
            starts.x, starts.y, starts.z,
            ends.x, ends.y, ends.z)

    fun inRange(startX: Int = 0,
                startY: Int = 0,
                startZ: Int = 0,
                endX: Int = 0,
                endY: Int = 0,
                endZ: Int = 0): Boolean =
            x in startX .. endX-1 &&
                    y in startY .. endY-1 &&
                    z in startZ .. endZ-1

    fun scaleSum(xScale: Int = 1, yScale: Int = 1, zScale: Int = 1): Int = x * xScale + y * yScale + z * zScale

    fun multiplyAll(xOffset: Int = 0, yOffset: Int = 0, zOffset: Int = 0): Int = (x+xOffset) * (y+yOffset) * (z+zOffset)

    // fun toVector3(): Vector3 = Vector3(x.toFloat(), y.toFloat(), z.toFloat())

    fun toInt2(): Int2  = Int2(x, y)

}

/*
fun Vector3.toInt3Rounded(): Int3 =
        Int3(MathUtils.round(this.x),
                MathUtils.round(this.y),
                MathUtils.round(this.z))

fun Vector3.toInt3Floored(): Int3 =
        Int3(MathUtils.fastFloor(this.x),
                MathUtils.fastFloor(this.y),
                MathUtils.fastFloor(this.z))
*/

