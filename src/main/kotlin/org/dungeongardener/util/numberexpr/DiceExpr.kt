package org.dungeongardener.util.numberexpr

/**
 *
 */
data class DiceExpr(val sides: Int = 6,
                    val count: Int = 1): NumExpr {

    override fun evaluate(context: NumContext): Double {
        var value = 0
        for (i in 1..count) {
            value += context.random.nextInt(sides) + 1
        }

        return value.toDouble()
    }

    override fun toString(): String {
        return "${count}D${sides}"
    }
}