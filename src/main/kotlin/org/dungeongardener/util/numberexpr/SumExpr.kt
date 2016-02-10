package org.dungeongardener.util.numberexpr

import org.dungeongardener.util.Context

/**
 *
 */
data class SumExpr(val a: NumExpr, val b: NumExpr): NumExpr {
    override fun evaluate(context: Context): Double {
        return a.evaluate(context) + b.evaluate(context)
    }

    override fun toString(): String {
        return "($a + $b)"
    }

}