package org.dungeongardener.util.numberexpr

/**
 *
 */
data class NegExpr(val e: NumExpr): NumExpr {
    override fun evaluate(context: NumContext): Double {
        return -e.evaluate(context)
    }

    override fun toString(): String {
        return "-$e"
    }
}