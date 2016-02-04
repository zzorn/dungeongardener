package org.dungeongardener.util.numberexpr

/**
 *
 */
data class DivExpr(val a: NumExpr, val b: NumExpr): NumExpr {
    override fun evaluate(context: NumContext): Double {
        return a.evaluate(context) / b.evaluate(context)
    }

    override fun toString(): String {
        return "($a / $b)"
    }
}