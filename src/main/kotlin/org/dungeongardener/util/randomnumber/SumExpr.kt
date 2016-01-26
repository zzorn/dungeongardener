package org.dungeongardener.util.randomnumber

/**
 *
 */
data class SumExpr(val a: NumExpr, val b: NumExpr): NumExpr {
    override fun evaluate(context: NumContext): Double {
        return a.evaluate(context) + b.evaluate(context)
    }

    override fun toString(): String {
        return "($a + $b)"
    }

}