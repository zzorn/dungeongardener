package org.dungeongardener.util.numberexpr

import org.dungeongardener.util.Context
import org.dungeongardener.util.genlang.nodes.Expression
import org.flowutils.random.RandomSequence

/**
 * Throws a dice with the specified number of sides the specified number of times and sums the result.
 */
// TODO: Move to RandomSequence
fun RandomSequence.dice(sides: Int = 6,
                        count: Int = 1): Int {
    var value = 0
    for (i in 1..count) {
        value += nextInt(sides) + 1
    }
    return value
}

/**
 *
 */
data class DiceExpr(val sides: Int = 6,
                    val count: Int = 1): Expression {

    override fun <T> evaluate(context: Context): T {
        return context.random.dice(sides, count).toDouble() as T
    }

    override fun toString(): String {
        return "${count}D${sides}"
    }
}

